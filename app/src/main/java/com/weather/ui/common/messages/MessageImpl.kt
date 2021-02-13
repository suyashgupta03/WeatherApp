package com.weather.ui.common.messages

import android.content.Context
import com.weather.R
import com.weather.entities.common.ErrorType

class MessageImpl(private val context: Context) : Message {

    override fun getErrorMessage(type: ErrorType): String {
        return when (type) {
            ErrorType.NETWORK -> context.getString(R.string.error_network)
            ErrorType.API_UNKNOWN_HOST -> context.getString(R.string.error_api_unknown_host)
            ErrorType.PARSING -> context.getString(R.string.error_unknown)
            ErrorType.UNKNOWN -> context.getString(R.string.error_unknown)
            ErrorType.NO_LOCATION -> context.getString(R.string.error_no_location)
        }
    }
}