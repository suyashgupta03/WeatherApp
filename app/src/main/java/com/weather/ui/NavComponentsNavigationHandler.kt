package com.weather.ui

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.weather.R
import com.weather.interactor.handlers.NavigationHandler
import com.weather.interactor.models.NavigationTarget
import com.weather.ui.fragments.ForecastDayDetailsFragment

class NavComponentsNavigationHandler(
    private val navController: NavController
) : NavigationHandler {

    override fun navigateTo(navigationTarget: NavigationTarget): Any =
        when (navigationTarget) {
            NavigationTarget.ForecastScreen -> {
                navController.navigate(
                    R.id.forecastFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
                )
            }
            is NavigationTarget.ForecastDayDetailsScreen -> {
                navController.navigate(
                    R.id.forecastDayDetailsFragment,
                    ForecastDayDetailsFragment.getBundle(navigationTarget.weatherDay),
                    slideAnimNavOptions
                )
            }
            NavigationTarget.SelectLocationScreen -> navController.navigate(
                R.id.selectLocationFragment,
                null,
                slideAnimNavOptions
            )
        }
}

private val slideAnimNavOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()