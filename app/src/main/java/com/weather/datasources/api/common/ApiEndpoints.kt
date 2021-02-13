package com.weather.datasources.api.common

import com.weather.datasources.api.models.ApiWeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoints {
    @GET("forecast/daily")
    suspend fun getWeatherForecast(
        @Query("q") city: String,
        @Query("cnt") count: Int,
        @Query("units") units: String
    ): Response<ApiWeatherForecast?>
}