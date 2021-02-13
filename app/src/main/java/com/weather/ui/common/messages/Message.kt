package com.weather.ui.common.messages

import com.weather.entities.common.ErrorType

interface Message {
    fun getErrorMessage(type: ErrorType): String
}