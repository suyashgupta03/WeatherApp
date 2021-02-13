package com.weather.datasources.api.common

import com.orhanobut.logger.Logger
import com.squareup.moshi.JsonEncodingException
import com.weather.datasources.api.common.connectivity.ConnectivityCheck
import com.weather.entities.common.AppResult
import com.weather.entities.common.ErrorType
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val RESPONSE_CODE_EMPTY = 204

sealed class ApiResponse<T> {

    companion object {
        fun <T> create(error: Throwable, check: ConnectivityCheck): ErrorResponse<T> {
            Logger.e(error, "Error while calling API")
            if(!check.isConnected()) { return ErrorResponse("no network", ErrorType.NETWORK)}
            return ErrorResponse(
                errorMessage = error.message ?: "unknown error",
                errorType = when (error) {
                    is ConnectException -> ErrorType.NETWORK
                    is SocketTimeoutException -> ErrorType.NETWORK
                    is UnknownHostException -> ErrorType.API_UNKNOWN_HOST
                    is IOException -> ErrorType.PARSING
                    is JsonEncodingException -> ErrorType.PARSING
                    else -> ErrorType.UNKNOWN
                },
                cause = error
            )
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == RESPONSE_CODE_EMPTY) {
                    EmptyResponse()
                } else {
                    SuccessResponse(body)
                }
            } else {
                val errorMsg = response.message()
                Logger.e("Error while calling API, response was not successful: $errorMsg")
                ErrorResponse(
                    errorMsg,
                    ErrorType.UNKNOWN
                )
            }
        }
    }

    fun <R> toAppResult(mapping: (T) -> R?): AppResult<R> {
        return try {
            when (this) {
                is SuccessResponse -> when (val result = mapping(body)) {
                    null -> {
                        Logger.e("Error while calling toAppResult, entity mapping returned null")
                        AppResult.Error(ErrorType.PARSING, "entity mapping returned null")
                    }
                    else -> AppResult.Success(result)
                }
                is EmptyResponse -> AppResult.Empty()
                is ErrorResponse -> AppResult.Error(type = errorType, message = errorMessage)
            }
        } catch (exception: Exception) {
            Logger.e(exception, "Error while calling toAppResult")
            AppResult.Error(
                type = ErrorType.UNKNOWN,
                message = exception.message ?: exception.localizedMessage,
                cause = exception
            )
        }
    }

    class EmptyResponse<T> : ApiResponse<T>()

    data class SuccessResponse<T>(val body: T) : ApiResponse<T>()

    data class ErrorResponse<T>(
        val errorMessage: String,
        val errorType: ErrorType,
        val cause: Throwable? = null
    ) : ApiResponse<T>()
}
