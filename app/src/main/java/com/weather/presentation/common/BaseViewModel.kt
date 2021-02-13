package com.weather.presentation.common

import androidx.lifecycle.ViewModel
import com.weather.interactor.models.NavigationTarget

typealias NavigationCommand = SingleLiveEvent<NavigationTarget>

abstract class BaseViewModel : ViewModel() {
    val navigationCommand = NavigationCommand()
}