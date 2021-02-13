package com.weather.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.weather.R
import com.weather.databinding.ActivityMainBinding
import com.weather.interactor.handlers.NavigationHandler
import com.weather.interactor.models.NavigationTarget
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_main.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private val navigationHandler: NavigationHandler? by inject {
        parametersOf(this, navController)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        navigationHandler?.navigateTo(NavigationTarget.ForecastScreen)
        setSupportActionBar(mainToolBar.toolbar)
        appBarConfiguration = AppBarConfiguration(navController.graph, null)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) { android.R.id.home -> onBackPressed() }
        return super.onOptionsItemSelected(item)
    }
}