package com.weather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.weather.R
import com.weather.databinding.FragmentForecastDayDetailsBinding
import com.weather.datasources.local.prefs.PrefData
import com.weather.entities.weather.WeatherDay
import com.weather.presentation.weather.ForecastDayDetailsViewModel
import com.weather.ui.common.BaseFragment
import com.weather.util.DateUtil
import com.weather.util.TimeOfDay
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastDayDetailsFragment :
    BaseFragment<ForecastDayDetailsViewModel, FragmentForecastDayDetailsBinding>() {

    override val viewModel: ForecastDayDetailsViewModel by viewModel()
    private val dateUtil: DateUtil by inject()
    private val prefData: PrefData by inject()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentForecastDayDetailsBinding = FragmentForecastDayDetailsBinding.inflate(
        inflater,
        container,
        false
    )

    override fun doBindings(
        binding: FragmentForecastDayDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.doBindings(binding, savedInstanceState)
        arguments?.getParcelable<WeatherDay>(ARG_WEATHER_DAY)?.let { weatherDay ->
            setData(
                binding,
                weatherDay
            )
        }
    }

    private fun setData(binding: FragmentForecastDayDetailsBinding, weatherDay: WeatherDay) {
        binding.tvLocation.text = getString(R.string.weather_day_location, prefData.getCachedCityName())
        binding.tvDate.text = getString(
            R.string.weather_day_details_date, dateUtil.convertEpochToLocalDate(
                weatherDay.date
            )
        )
        binding.tvCurrentWeather.text = getString(
            R.string.weather_day_status,
            weatherDay.weather.first().main
        )
        when (dateUtil.getTimeOfDay()) {
            TimeOfDay.MORNING -> {
                binding.tvCurrentTemp.text = getString(
                    R.string.weather_day_temp,
                    weatherDay.temp.morn.toInt()
                )
                binding.tvFeelsLike.text = getString(
                    R.string.weather_day_feels_like,
                    weatherDay.feelsLike.morn.toInt()
                )
            }
            TimeOfDay.EVENING -> {
                binding.tvCurrentTemp.text = getString(
                    R.string.weather_day_temp,
                    weatherDay.temp.eve.toInt()
                )
                binding.tvFeelsLike.text = getString(
                    R.string.weather_day_feels_like,
                    weatherDay.feelsLike.eve.toInt()
                )
            }
            TimeOfDay.NIGHT -> {
                binding.tvCurrentTemp.text = getString(
                    R.string.weather_day_temp,
                    weatherDay.temp.night.toInt()
                )
                binding.tvFeelsLike.text = getString(
                    R.string.weather_day_feels_like,
                    weatherDay.feelsLike.night.toInt()
                )
            }
            TimeOfDay.AFTERNOON -> {
                binding.tvCurrentTemp.text = getString(
                    R.string.weather_day_temp,
                    weatherDay.temp.day.toInt()
                )
                binding.tvFeelsLike.text = getString(
                    R.string.weather_day_feels_like,
                    weatherDay.feelsLike.day.toInt()
                )
            }
        }
        binding.tvMinTemp.text = getString(
            R.string.weather_day_temp_min,
            weatherDay.min.toInt()
        )
        binding.tvMaxTemp.text = getString(
            R.string.weather_day_temp_max,
            weatherDay.max.toInt()
        )
        binding.tvPressure.text = getString(R.string.weather_day_pressure, weatherDay.pressure)
        binding.tvHumidity.text = getString(R.string.weather_day_humidity, weatherDay.humidity)
        binding.tvSunrise.text = getString(
            R.string.weather_day_details_sunrise, dateUtil.convertEpochToLocalTime(
                weatherDay.sunrise
            )
        )
        binding.tvSunset.text = getString(
            R.string.weather_day_details_sunset, dateUtil.convertEpochToLocalTime(
                weatherDay.sunset
            )
        )
        binding.tvWindSpeed.text = getString(
            R.string.weather_day_details_speed,
            weatherDay.speed.toString()
        )
    }

    companion object {
        private const val ARG_WEATHER_DAY = "argWeatherDay"

        fun getBundle(data: WeatherDay): Bundle {
            return Bundle().also {
                it.putParcelable(ARG_WEATHER_DAY, data)
            }
        }
    }
}