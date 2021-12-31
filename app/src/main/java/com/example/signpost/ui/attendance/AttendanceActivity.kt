package com.example.signpost.ui.attendance

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.networkdomain.model.EmployeeDto
import com.example.signpost.R
import com.example.signpost.base.BaseActivity
import com.example.signpost.base.ViewState
import com.example.signpost.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AttendanceActivity : BaseActivity<ActivityMainBinding>() {

    override fun layoutId(): Int = R.layout.activity_attendance


    private val _viewModel: AttendanceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()


        addListener()
        addObservers()
    }


    private fun addListener() {
    }




    override fun addObservers() {
        observeEmployeeList()
        observeViewState()
        observeError()
    }

    private fun observeViewState() {
        _viewModel.viewState.observe(this) {
            when (it) {
                ViewState.Loading -> {
                    uiUtil.showProgress()
                }
                ViewState.Success() -> {
                    uiUtil.hideProgress()
                }
                else -> {
                    uiUtil.hideProgress()
                }
            }
        }
    }

    private fun observeError() {
        _viewModel.setError.observe(this) {
            showToast(it.toString())
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


}





