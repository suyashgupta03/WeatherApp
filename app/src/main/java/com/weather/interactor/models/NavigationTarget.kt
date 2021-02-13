package com.weather.interactor.models

import com.weather.entities.weather.WeatherDay

sealed class NavigationTarget {
    object ForecastScreen : NavigationTarget()
    data class ForecastDayDetailsScreen(val weatherDay: WeatherDay) : NavigationTarget()
    object SelectLocationScreen : NavigationTarget()
}