package com.weather.datasources.api.common.connectivity

interface ConnectivityCheck {
    fun isConnected(): Boolean
}