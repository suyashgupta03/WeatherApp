package com.weather.datasources.api

import com.orhanobut.logger.Logger
import com.weather.BuildConfig.SERVER_URL
import com.weather.datasources.api.common.ApiEndpoints
import com.weather.datasources.api.common.ApiResponse
import com.weather.datasources.api.common.connectivity.ConnectivityCheck
import com.weather.datasources.api.models.toObjOrNull
import com.weather.entities.common.AppResult
import com.weather.entities.weather.WeatherForecast
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiDataSourceImpl(okHttpClient: OkHttpClient, private val connectivityCheck: ConnectivityCheck) :
    ApiDataSource {

    private val service = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(SERVER_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(ApiEndpoints::class.java)

    override suspend fun fetchWeatherForecast(
        city: String,
        count: Int,
        unit: String
    ): AppResult<WeatherForecast> {
        return asApiResponse(connectivityCheck) {
            service.getWeatherForecast(city, count, unit)
        }.toAppResult { weatherForecast ->
            weatherForecast?.toObjOrNull()
        }
    }
}

@Suppress("TooGenericExceptionCaught")
suspend fun <T> asApiResponse(connectivityCheck: ConnectivityCheck, call: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        ApiResponse.create(call())
    } catch (ex: Exception) {
        Logger.e("Error: ${ex.localizedMessage}", "ApiDataSource", ex)
        ApiResponse.create(ex, connectivityCheck)
    }
}

const val API_CALL_TIMEOUT = 10L //secs