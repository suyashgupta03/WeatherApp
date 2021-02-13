package com.weather.ui.fragments

import android.os.Bundle
import android.view.*
import com.weather.R
import com.weather.databinding.FragmentForecastBinding
import com.weather.datasources.local.prefs.PrefData
import com.weather.entities.common.AppResult
import com.weather.entities.common.LoadingEvent
import com.weather.presentation.weather.ForecastViewModel
import com.weather.ui.adapter.ForecastAdapter
import com.weather.ui.common.BaseFragment
import com.weather.ui.common.messages.Message
import com.weather.ui.common.updateWithLoadingEvent
import com.weather.util.DateUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastFragment : BaseFragment<ForecastViewModel, FragmentForecastBinding>() {

    override val viewModel: ForecastViewModel by viewModel()

    private val messages: Message by inject()
    private val dateUtil: DateUtil by inject()
    private val prefData: PrefData by inject()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentForecastBinding {
        setHasOptionsMenu(true)
        return FragmentForecastBinding.inflate(inflater, container, false)
    }

    override fun doBindings(binding: FragmentForecastBinding, savedInstanceState: Bundle?) {
        super.doBindings(binding, savedInstanceState)
        viewModel.forecast.observe(viewLifecycleOwner, { loadingEvent ->
            binding.loadingContainer.updateWithLoadingEvent(loadingEvent)
            when (loadingEvent) {
                is LoadingEvent.Loading -> {
                }
                is LoadingEvent.Result -> {
                    when (val appResult = loadingEvent.result) {
                        is AppResult.Success -> {
                            val adapter = ForecastAdapter(childFragmentManager, dateUtil)
                            adapter.addData(appResult.latestValue.weatherDay)
                            prefData.saveCachedCityName(appResult.latestValue.city.name+","+appResult.latestValue.city.country)
                            binding.pagerForecast.adapter = adapter
                        }
                        is AppResult.Error -> {
                            val errorMsg = messages.getErrorMessage(appResult.type)
                            binding.loadingContainer.loadingErrorText.text = errorMsg
                        }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_forecast, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_location) {
            viewModel.showLocationSelection()
        }
        return super.onOptionsItemSelected(item)
    }
}