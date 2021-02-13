package com.weather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.weather.R
import com.weather.databinding.FragmentForecastDayBinding
import com.weather.datasources.local.prefs.PrefData
import com.weather.entities.weather.WeatherDay
import com.weather.presentation.weather.ForecastDayViewModel
import com.weather.ui.common.BaseFragment
import com.weather.util.DateUtil
import com.weather.util.TimeOfDay
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastDayFragment : BaseFragment<ForecastDayViewModel, FragmentForecastDayBinding>() {

    override val viewModel: ForecastDayViewModel by viewModel()
    private val dateUtil: DateUtil by inject()
    private val prefData: PrefData by inject()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentForecastDayBinding = FragmentForecastDayBinding.inflate(inflater, container, false)

    override fun doBindings(binding: FragmentForecastDayBinding, savedInstanceState: Bundle?) {
        super.doBindings(binding, savedInstanceState)
        arguments?.getParcelable<WeatherDay>(ARG_WEATHER_DAY)?.let { weatherDay -> setData(binding, weatherDay) }
    }

    private fun setData(binding: FragmentForecastDayBinding, weatherDay: WeatherDay) {
        binding.tvLocation.text = getString(R.string.weather_day_location, prefData.getCachedCityName())
        binding.tvCurrentWeather.text = getString(R.string.weather_day_status, weatherDay.weather.first().main)
        when(dateUtil.getTimeOfDay()) {
            TimeOfDay.MORNING -> {
                binding.tvCurrentTemp.text = getString(R.string.weather_day_temp, weatherDay.temp.morn.toInt())
                binding.tvFeelsLike.text = getString(R.string.weather_day_feels_like, weatherDay.feelsLike.morn.toInt())
            }
            TimeOfDay.EVENING -> {
                binding.tvCurrentTemp.text = getString(R.string.weather_day_temp, weatherDay.temp.eve.toInt())
                binding.tvFeelsLike.text = getString(R.string.weather_day_feels_like, weatherDay.feelsLike.eve.toInt())
            }
            TimeOfDay.NIGHT -> {
                binding.tvCurrentTemp.text = getString(R.string.weather_day_temp, weatherDay.temp.night.toInt())
                binding.tvFeelsLike.text = getString(R.string.weather_day_feels_like, weatherDay.feelsLike.night.toInt())
            }
            TimeOfDay.AFTERNOON -> {
                binding.tvCurrentTemp.text = getString(R.string.weather_day_temp, weatherDay.temp.day.toInt())
                binding.tvFeelsLike.text = getString(R.string.weather_day_feels_like, weatherDay.feelsLike.day.toInt())
            }
        }
        binding.tvMinTemp.text = getString(R.string.weather_day_temp_min, weatherDay.min.toInt())
        binding.tvMaxTemp.text = getString(R.string.weather_day_temp_max, weatherDay.max.toInt())
        binding.tvPressure.text = getString(R.string.weather_day_humidity, weatherDay.pressure)
        binding.tvHumidity.text = getString(R.string.weather_day_temp_max, weatherDay.humidity)
        binding.btnMoreDetails.setOnClickListener {
            viewModel.showDetails(weatherDay)
        }
    }

    companion object {
        private const val ARG_WEATHER_DAY = "argWeatherDay"
        fun instanceOf(data: WeatherDay): ForecastDayFragment {
            val fragment = ForecastDayFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(ARG_WEATHER_DAY, data)
            }
            return fragment
        }
    }
}