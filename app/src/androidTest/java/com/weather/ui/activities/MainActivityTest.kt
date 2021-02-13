package com.weather.ui.activities

import android.content.Context
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.weather.R
import com.weather.datasources.local.prefs.PrefData
import com.weather.datasources.local.prefs.PrefDataImpl
import com.weather.datasources.local.prefs.SharedPreference
import com.weather.datasources.local.prefs.SharedPreferenceImpl
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest {

    private lateinit var cityName: String
    private lateinit var prefs: SharedPreference
    private lateinit var prefData: PrefData

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        cityName = "leipzig"
    }

    @Test
    fun changeCity_mainActivity() {
        //main screen
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText("Weather Forecast")))
        // Type text and then press the button.
        onView(withId(R.id.item_location))
            .perform(click())

        //city change screen
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText("Add a location or use GPS")))

        // Type text and then press the button.
        onView(withId(R.id.etLocation))
            .perform(typeText(cityName), closeSoftKeyboard())
        onView(withId(R.id.btnLocationOk)).perform(click())

        //back to main screen
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText("Weather Forecast")))
    }

    @Test
    fun checkWeatherData_mainActivity() {
        //set city
        val context: Context = getInstrumentation().targetContext
        prefs = SharedPreferenceImpl(context)
        prefData = PrefDataImpl(prefs)
        prefData.saveCachedCityName(cityName)

        //main screen
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText("Weather Forecast")))

        //check page is visible
        onView(allOf(withId(R.id.pager_forecast), isCompletelyDisplayed()))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        //click for first more details button
        onView(allOf(withId(R.id.btnMoreDetails), isCompletelyDisplayed())).perform(click())

        //check the details screen launch
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText("Forecast Details")))

        //check details screen data
        onView(allOf(instanceOf(TextView::class.java), withId(R.id.tvWindSpeed), isCompletelyDisplayed()))
    }
}