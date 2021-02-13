package com.weather.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.weather.entities.weather.WeatherDay
import com.weather.ui.fragments.ForecastDayFragment
import com.weather.util.DateUtil

class ForecastAdapter(fm: FragmentManager, private val dateUtil: DateUtil) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val weatherDays: MutableList<WeatherDay> = mutableListOf()
    private val titleDateList: MutableList<Long> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return ForecastDayFragment.instanceOf(weatherDays[position])
    }

    override fun getCount(): Int {
        return weatherDays.size
    }

    fun addData(list: List<WeatherDay>) {
        weatherDays.addAll(list)
        titleDateList.addAll(list.map { it.date }.toList())
    }

    override fun getPageTitle(position: Int): CharSequence {
        return dateUtil.convertEpochToLocalDate(titleDateList[position])
    }
}