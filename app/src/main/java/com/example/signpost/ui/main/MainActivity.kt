package com.example.signpost.ui.main

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
import com.example.signpost.ui.main.graph.GraphFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun layoutId(): Int = R.layout.activity_main
    private val employeeList = ArrayList<EmployeeDto>()


    private val _viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        addFragmentToActivity(GraphFragment())

        binding.btAttendance.setOnClickListener {
           addFragmentToActivity(GraphFragment())
        }
        addListener()
        addObservers()
    }

    private fun addFragmentToActivity(fragment: Fragment?){

        if (fragment == null) return
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        tr.add(R.id.frame_container, fragment)
        tr.commitAllowingStateLoss()
    }
    private fun addListener() {
        with(binding) {
            spnrEmployee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
            spnrMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
            spnrYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }


    private fun initUi() {

        _viewModel.getAllEmployee()
    }


    override fun addObservers() {
        observeEmployeeList()
        observeViewState()
        observeError()
    }

    private fun observeViewState() {
        _viewModel.viewState.observe(this, {
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
        })
    }

    private fun observeError() {
        _viewModel.setError.observe(this, {
            showToast(it.toString())
        })
    }

    private fun observeEmployeeList() {
        _viewModel.setEmployeeList.observe(this, {
            if (it == null || it.isEmpty()) {
                uiUtil.showToast("Something went wrong")
                return@observe
            }
            employeeList.clear()
            employeeList.addAll(it)
            val employeeName = ArrayList<String>();
            employeeName.add("Select Employee")
            val list = employeeList.map { it.name }
            employeeName.addAll(list as (ArrayList<String>))
            binding.spnrEmployee.adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, employeeName)
        })
    }

    override fun onBackPressed() {
        employeeList.clear()
        super.onBackPressed()
    }


}





