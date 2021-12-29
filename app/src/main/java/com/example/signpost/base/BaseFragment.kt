package com.example.signpost.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.signpost.util.UiUtil
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    protected lateinit var binding: B
    protected lateinit var uiUtil: UiUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   viewModel = if (bindToActivityViewModel.not())
        /*      ViewModelProvider(this, viewModelFactory)[getViewModelClass()]
        else
            activity?.run {
                ViewModelProvider(this, viewModelFactory)[getViewModelClass()]
            } ?: throw Exception("Invalid Activity")*/
        uiUtil = UiUtil(requireContext())
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
    }

    @LayoutRes
    abstract fun getLayout(): Int
    abstract fun addObservers()


    protected fun showProgress() {
        uiUtil.showProgress()
    }

    protected fun hideProgress() {
        uiUtil.hideProgress()
    }

    protected fun showMessage(message: String?) {
        message?.let { uiUtil.showMessage(it) }
    }

    protected fun showToast(
        message: String?,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        message?.let {
            Toast.makeText(requireContext(), it, duration)
                .show()
        }
    }


}

