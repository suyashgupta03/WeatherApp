package com.weather

import android.app.Activity
import androidx.navigation.NavController
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.weather.datasources.api.API_CALL_TIMEOUT
import com.weather.datasources.api.ApiDataSource
import com.weather.datasources.api.ApiDataSourceImpl
import com.weather.datasources.api.common.connectivity.ConnectivityCheck
import com.weather.datasources.api.common.connectivity.ConnectivityCheckImpl
import com.weather.datasources.api.common.NetworkHeaderInterceptor
import com.weather.datasources.local.prefs.PrefData
import com.weather.datasources.local.prefs.PrefDataImpl
import com.weather.datasources.local.prefs.SharedPreference
import com.weather.datasources.local.prefs.SharedPreferenceImpl
import com.weather.datasources.local.room.DbDataSource
import com.weather.datasources.local.room.RoomDataSource
import com.weather.datasources.local.room.common.APP_DB_NAME
import com.weather.datasources.local.room.common.AppRmDatabase
import com.weather.interactor.handlers.NavigationHandler
import com.weather.interactor.usecases.WeatherUseCase
import com.weather.interactor.usecases.WeatherUseCaseImpl
import com.weather.presentation.weather.ForecastDayDetailsViewModel
import com.weather.presentation.weather.ForecastDayViewModel
import com.weather.presentation.weather.ForecastViewModel
import com.weather.presentation.weather.SelectLocationViewModel
import com.weather.ui.NavComponentsNavigationHandler
import com.weather.ui.common.messages.Message
import com.weather.ui.common.messages.MessageImpl
import com.weather.util.DateUtil
import com.weather.util.DateUtilImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object Koin {
    private val appModule = module {
        factory<NavigationHandler> { (_: Activity?, navController: NavController) ->
            NavComponentsNavigationHandler(
                navController
            )
        }
    }

    private val interactorModule = module {
        factory<WeatherUseCase> { WeatherUseCaseImpl(get(), get(), get(), get()) }
    }

    private val dataSourceModule = module {
        single<ApiDataSource> { ApiDataSourceImpl(get(), get()) }
        factory {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .callTimeout(API_CALL_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(NetworkHeaderInterceptor())
                .build()
        }
        single<SharedPreference> { SharedPreferenceImpl(androidContext()) }
        single<PrefData> { PrefDataImpl(get()) }
        single {
            Room.databaseBuilder(androidApplication(), AppRmDatabase::class.java, APP_DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
        single<DbDataSource> { RoomDataSource(get()) }
    }

    private val presentationModule = module {
        viewModel { ForecastViewModel(get()) }
        viewModel { ForecastDayViewModel() }
        viewModel { ForecastDayDetailsViewModel() }
        viewModel { SelectLocationViewModel() }
    }

    private val utilityModule = module {
        single<DateUtil> { DateUtilImpl() }
        single<ConnectivityCheck> { ConnectivityCheckImpl(androidContext()) }
    }

    private val uiModule = module {
        single<Message> { MessageImpl(get()) }
    }

    val modules = listOf(
        appModule,
        interactorModule,
        dataSourceModule,
        presentationModule,
        utilityModule,
        uiModule
    )
}
