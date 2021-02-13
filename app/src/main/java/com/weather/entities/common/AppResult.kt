package com.weather.entities.common

sealed class AppResult<out T>(open val latestValue: T?) {

    data class Success<out T>(
        override val latestValue: T
    ) : AppResult<T>(latestValue)

    class Empty<T> : AppResult<T>(null)

    data class Error(
        val type: ErrorType = ErrorType.UNKNOWN,
        val message: String,
        val cause: Throwable? = null
    ) : AppResult<Nothing>(null)
}