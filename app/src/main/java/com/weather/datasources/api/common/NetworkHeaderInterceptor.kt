package com.weather.datasources.api.common

import com.weather.BuildConfig.KEY_WEATHER_API
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("x-rapidapi-key", KEY_WEATHER_API)
        builder.addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
        return chain.proceed(builder.build())
    }
}