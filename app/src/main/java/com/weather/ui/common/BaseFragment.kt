package com.weather.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.weather.interactor.handlers.NavigationHandler
import com.weather.presentation.common.BaseViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class BaseFragment<Model : BaseViewModel, Binding : ViewBinding> : Fragment() {

    protected abstract val viewModel: Model
    private var binding: Binding? = null

    protected open val navigationHandler: NavigationHandler by inject {
        parametersOf(context, findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createBinding(inflater, container).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doBindings(binding ?: return, savedInstanceState)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): Binding

    @CallSuper
    open fun doBindings(binding: Binding, savedInstanceState: Bundle?) {
        viewModel.navigationCommand.observe(viewLifecycleOwner, Observer { navigationTarget ->
            navigationHandler.navigateTo(navigationTarget)
        })
    }
}