package com.weather.entities.common

sealed class LoadingEvent<out T> {
    object Loading : LoadingEvent<Nothing>()
    data class Result<T>(val result: AppResult<T>) : LoadingEvent<T>()
}
