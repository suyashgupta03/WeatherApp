package com.weather.interactor

import androidx.lifecycle.MutableLiveData
import com.weather.entities.common.AppResult
import com.weather.entities.common.LoadingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

fun <T> CoroutineScope.launchWithLoadingEvent(
    loadingEvent: MutableLiveData<LoadingEvent<T>>,
    function: suspend () -> AppResult<T>
) {
    loadingEvent.postValue(LoadingEvent.Loading)
    launch(IO) {
        loadingEvent.postValue(LoadingEvent.Result(function()))
    }
}
