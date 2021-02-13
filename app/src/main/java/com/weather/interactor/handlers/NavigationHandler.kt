package com.weather.interactor.handlers

import com.weather.interactor.models.NavigationTarget

interface NavigationHandler {
    fun navigateTo(navigationTarget: NavigationTarget): Any?
}