package com.example.signpost.ui.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.model.RequestAttendanceDto
import com.example.signpost.R
import com.example.signpost.base.BaseActivity
import com.example.signpost.base.ViewState
import com.example.signpost.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun layoutId(): Int = R.layout.activity_main
    private val employeeList = ArrayList<EmployeeDto>()

    private val fromDate=""
    private val empId=""
    private val toDate=""
    val calendar: Calendar = Calendar.getInstance()


    private val _viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        addListener()
        addObservers()
    }


    private fun addListener() {
        with(binding) {
            spnrEmployee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    for (item in employeeList) {
                        if(item.name.equals(binding.spnrEmployee.getSelectedItem().toString())){
                            empId=binding.spnrEmployee.getSelectedItem().toString()
                        }
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            imgFromDate.setOnClickListener{
                val mYear = calendar.get(Calendar.YEAR)
                val mMonth = calendar.get(Calendar.MONTH)
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    this@MainActivity, R.style.AppCompatAlertDialogStyle,
                    { _, year, monthOfYear, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val myFormat = "yyyy-MM-dd"
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        binding.tvFromDate = sdf.format(calendar.time)
                        openTimePickerDialog(true)
                    }, mYear, mMonth, mDay
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }
            imgToDate.setOnClickListener{
                val mYear = calendar.get(Calendar.YEAR)
                val mMonth = calendar.get(Calendar.MONTH)
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    this@MainActivity, R.style.AppCompatAlertDialogStyle,
                    { _, year, monthOfYear, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val myFormat = "yyyy-MM-dd"
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        binding.tvToDate = sdf.format(calendar.time)
                        openTimePickerDialog(false)
                    }, mYear, mMonth, mDay
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }
            binding.btAttendance.setOnClickListener {
                if(empId.isBlank()){
                    showToast("Please Select employee")
                    return@setOnClickListener
                }
                val requestAttendanceDto=RequestAttendanceDto()
                requestAttendanceDto.emp=empId
                requestAttendanceDto.from_dt=fromDate
                requestAttendanceDto.to_dt=toDate
                _viewModel.getAttendance()
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun openTimePickerDialog(fromDate:Boolean) {
        val hourOfDayCal = calendar.get(Calendar.HOUR_OF_DAY)
        val minuteCal = calendar.get(Calendar.MINUTE)
        val timePickerDialog =
            TimePickerDialog(this, R.style.AppCompatAlertDialogStyle, { _, hourOfDay, minute ->
                val amPm: String
                var hour = hourOfDay
                when {
                    hour == 0 -> {
                        hour += 12
                        amPm = "AM"
                    }
                    hour == 12 -> amPm = "PM"
                    hour > 12 -> {
                        hour -= 12
                        amPm = "PM"
                    }
                    else -> amPm = "AM"
                }
                val hourCurrent = if (hour < 10) "0$hour" else hour
                val minCurrent = if (minute < 10) "0$minute" else minute
                val time = "$hourCurrent:$minCurrent $amPm"
                if(fromDate){
                    fromDate = "$dateAndTime $time"
                }else{
                    toDate="$dateAndTime $time"
                }
            }, hourOfDayCal, minuteCal, false)
        timePickerDialog.show()
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

    private fun observeEmployeeList() {
        _viewModel.setEmployeeList.observe(this) {
            if (it == null || it.isEmpty()) {
                uiUtil.showToast("Something went wrong")
                return@observe
            }
            employeeList.clear()
            employeeList.addAll(it)
            val employeeName = ArrayList<String>();
            employeeName.add("Select Employee")
            val list = employeeList.map {
                if(it.name.isNullOrBlank().not()) {
                    it.name
                }
            }
            employeeName.addAll(list as (ArrayList<String>))
            binding.spnrEmployee.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, employeeName)
        }
    }

    override fun onBackPressed() {
        employeeList.clear()
        super.onBackPressed()
    }


}





