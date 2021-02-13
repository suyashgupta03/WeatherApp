package com.weather.ui.common

import androidx.core.view.isVisible
import com.weather.databinding.ProgressBarAndErrorLabelBinding
import com.weather.entities.common.AppResult
import com.weather.entities.common.LoadingEvent

fun <T> ProgressBarAndErrorLabelBinding.updateWithLoadingEvent(
    loadingEvent: LoadingEvent<T>?
) {
    when (loadingEvent) {
        is LoadingEvent.Loading -> {
            loadingIndicator.isVisible = true
        }
        is LoadingEvent.Result -> {
            loadingIndicator.isVisible = false
            when (loadingEvent.result) {
                is AppResult.Error -> {
                    loadingErrorText.isVisible = true
                }
                else -> {}
            }
        }
    }
}