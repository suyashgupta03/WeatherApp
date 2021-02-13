package com.weather.presentation.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weather.entities.common.LoadingEvent
import com.weather.entities.weather.WeatherForecast
import com.weather.interactor.launchWithLoadingEvent
import com.weather.interactor.models.NavigationTarget
import com.weather.interactor.usecases.WeatherUseCase
import com.weather.presentation.common.BaseViewModel

class ForecastViewModel(weatherUseCase: WeatherUseCase) : BaseViewModel() {

    val forecast = MutableLiveData<LoadingEvent<WeatherForecast>>()

    init {
        viewModelScope.launchWithLoadingEvent(forecast) {
            weatherUseCase.getForecastOfOneWeek()
        }
    }

    fun showLocationSelection() {
        navigationCommand.postValue(NavigationTarget.SelectLocationScreen)
    }
}