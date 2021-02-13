package com.weather.presentation.weather

import com.weather.entities.weather.WeatherDay
import com.weather.interactor.models.NavigationTarget
import com.weather.presentation.common.BaseViewModel

class ForecastDayViewModel : BaseViewModel() {

    fun showDetails(weatherDay: WeatherDay) {
        navigationCommand.postValue(NavigationTarget.ForecastDayDetailsScreen(weatherDay))
    }
}