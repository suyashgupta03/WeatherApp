package com.weather.interactor.usercases

import com.nhaarman.mockitokotlin2.any
import com.weather.datasources.api.ApiDataSource
import com.weather.datasources.local.prefs.PrefData
import com.weather.datasources.local.room.DbDataSource
import com.weather.entities.common.AppResult
import com.weather.entities.common.ErrorType
import com.weather.interactor.usecases.WeatherUseCase
import com.weather.interactor.usecases.WeatherUseCaseImpl
import com.weather.testutil.CoroutineTestRule
import com.weather.testutil.DummyData
import com.weather.testutil.DummyData.cityName
import com.weather.testutil.DummyData.noCityErrMessage
import com.weather.ui.common.messages.Message
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class WeatherUseCaseTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val apiDataSource: ApiDataSource = Mockito.mock(ApiDataSource::class.java)
    private val dbDataSource: DbDataSource = Mockito.mock(DbDataSource::class.java)
    private val prefDataSource: PrefData = Mockito.mock(PrefData::class.java)
    private val messageSource: Message = Mockito.mock(Message::class.java)
    private lateinit var useCase: WeatherUseCase

    @Before
    fun setup() {
        useCase = WeatherUseCaseImpl(apiDataSource, dbDataSource, prefDataSource, messageSource)
    }

    @Test
    fun `check success of weather forecast from weather use case`() = coroutineTestRule.testDispatcher.runBlockingTest{
        Mockito.`when`(apiDataSource.fetchWeatherForecast(any(), any(), any())).thenReturn(AppResult.Success(
            DummyData.weatherForecast()))
        Mockito.`when`(prefDataSource.getCachedCityName()).thenReturn(cityName)
        when(val result = useCase.getForecastOfOneWeek()) {
            is AppResult.Success -> {
                val forecast = result.latestValue
                Assert.assertEquals(7, forecast.count)
                Assert.assertEquals("Leipzig", forecast.city.name)
            }
            is AppResult.Empty -> assert(false) { "Success should be called instead of empty" }
            is AppResult.Error -> assert(false) { "Success should be called instead of error" }
        }
    }

    @Test
    fun `check no city error of weather forecast from weather use case`() = coroutineTestRule.testDispatcher.runBlockingTest{
        Mockito.`when`(prefDataSource.getCachedCityName()).thenReturn("")
        Mockito.`when`(messageSource.getErrorMessage(any())).thenReturn(noCityErrMessage())
        when(val result = useCase.getForecastOfOneWeek()) {
            is AppResult.Success -> assert(false) { "Error should be called instead of success" }
            is AppResult.Empty -> assert(false) { "Error should be called instead of success" }
            is AppResult.Error -> {
                Assert.assertEquals(ErrorType.NO_LOCATION, result.type)
                Assert.assertEquals("noCitySet", result.message)
            }
        }
    }

    @Test
    fun `check no network error of weather forecast from weather use case`() = coroutineTestRule.testDispatcher.runBlockingTest{
        Mockito.`when`(prefDataSource.getCachedCityName()).thenReturn(cityName)
        Mockito.`when`(apiDataSource.fetchWeatherForecast(any(), any(), any())).thenReturn(AppResult.Error(
            ErrorType.NETWORK, "no internet"))
        when(val result = useCase.getForecastOfOneWeek()) {
            is AppResult.Success -> assert(false) { "Error should be called instead of success" }
            is AppResult.Empty -> assert(false) { "Error should be called instead of success" }
            is AppResult.Error -> {
                Assert.assertEquals(ErrorType.NETWORK, result.type)
                Assert.assertEquals("no internet", result.message)
            }
        }
    }
}