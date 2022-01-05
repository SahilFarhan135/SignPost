package com.example.signpost.ui.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.model.FirebaseObj
import com.example.networkdomain.storage.PrefsUtil
import com.example.signpost.BuildConfig
import com.example.signpost.R
import com.example.signpost.base.BaseActivity
import com.example.signpost.base.ViewState
import com.example.signpost.databinding.ActivityMainBinding
import com.example.signpost.ui.attendance.AttendanceActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun layoutId(): Int = R.layout.activity_main
    private val employeeList = ArrayList<EmployeeDto>()

    private var fromDate = false
    private var empId: String? = ""
    private var empName: String? = ""
    private var responseCode = "400"

    private var toDate = false
    private val calendar: Calendar = Calendar.getInstance()
    private var employeeHashMap = HashMap<String, String>()
    private var localList = ArrayList<String>()
    private var attendanceList = ArrayList<AttendanceDto>()


    private var tvToDatee: Date? = null
    private var tvFromDatee: Date? = null
    private var firstTimeAttendance = false
    private var check = false
    private var firstTimeResponse = false

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
                    empId = employeeHashMap[spnrEmployee.selectedItem.toString()]
                    empName = spnrEmployee.selectedItem.toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            imgFromDate.setOnClickListener {
                val mYear = calendar.get(Calendar.YEAR)
                val mMonth = calendar.get(Calendar.MONTH)
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    this@MainActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val myFormat = "yyyy-MM-dd"
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        binding.tvFromDate.text = sdf.format(calendar.time)
                        tvFromDatee = sdf.parse(binding.tvFromDate.text.toString())
                        fromDate = true
                    }, mYear, mMonth, mDay
                )
                datePickerDialog.datePicker.minDate = 1564617600000//aug 2019
                datePickerDialog.datePicker.maxDate = 1580601599000//feb 2021
                datePickerDialog.show()
            }
            imgToDate.setOnClickListener {
                val mYear = calendar.get(Calendar.YEAR)
                val mMonth = calendar.get(Calendar.MONTH)
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    this@MainActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val myFormat = "yyyy-MM-dd"
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        binding.tvToDate.text = sdf.format(calendar.time)
                        tvToDatee = sdf.parse(binding.tvToDate.text.toString())
                        //1564617600
                        toDate = true
                    }, mYear, mMonth, mDay
                )
                datePickerDialog.datePicker.minDate = 1564617600000//aug 2019
                datePickerDialog.datePicker.maxDate = 1580601599000//feb 2021

                datePickerDialog.show()
            }
            binding.btAttendance.setOnClickListener {
                if (empId.isNullOrBlank()) {
                    showToast("Please Select employee")
                    return@setOnClickListener
                }
                if (empId.equals("null", true)) {
                    showToast("EmployeeId for selected Employee is invalid.")
                    return@setOnClickListener
                }
                /*val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("body", requestAttendanceDto.toString())
                    .build()*/
                _viewModel.getAttendancee(
                    Triple(
                        empId!!,
                        binding.tvFromDate.text.toString(),
                        binding.tvToDate.text.toString()
                    )
                )
            }
        }
    }


    private fun initUi() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            val pref = PrefsUtil(
                this.getSharedPreferences(
                    PrefsUtil.SHARED_PREFERENCE_ID,
                    Context.MODE_PRIVATE
                )
            )
            pref.fcmToken = token
        })
        _viewModel.getAllEmployee()
        binding.appbar.btBack.visibility = View.GONE
    }


    override fun addObservers() {
        observeEmployeeList()
        observeAttendanceList()
        observeViewState()
        observeStatusCode()
    }


    private fun observeStatusCode() {
        _viewModel.setResponseCodeForAttendanceList.observe(this) {
            if (firstTimeResponse) {
                firstTimeResponse = firstTimeResponse.not()
                if (it != null) {
                    responseCode = it
                }
            } else {
                firstTimeResponse = firstTimeResponse.not()
            }
        }
    }


    private fun observeAttendanceList() {
        _viewModel.setAttendanceList.observe(this) {
            if (firstTimeAttendance) {
                attendanceList.clear()
                firstTimeAttendance = firstTimeAttendance.not()
                if (it == null || it.isNullOrEmpty()) {
                    openDialogForFakeData(it)
                    uiUtil.showToast("No Data Found")
                    return@observe
                }
                attendanceList.addAll(it)
                saveDataToFirebase()
            } else {
                firstTimeAttendance = firstTimeAttendance.not()
            }

        }

    }

    private fun openDialogForFakeData(list: List<AttendanceDto>?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Use Fake Data")
        builder.setMessage("Do you want to use fake data?")
        builder.setPositiveButton(
            "YES"
        ) { dialog, _ ->
            attendanceList.addAll(getDataFromFakeRepository())
            saveDataToFirebase()
            dialog.dismiss()
        }
        builder.setNegativeButton(
            "NO"
        ) { dialog, _ ->
            attendanceList.addAll(list ?: emptyList())
            saveDataToFirebase()
            dialog.dismiss()
        }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun saveDataToFirebase() {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName
        val obj = FirebaseObj()
        obj.androidVersion = version
        obj.appVersion = BuildConfig.VERSION_NAME
        obj.employeeName = empName
        obj.mobileMake = Build.MANUFACTURER
        obj.mobileModel = Build.MODEL
        obj.responseStatus = responseCode
        obj.fromDate = binding.tvFromDate.text.toString()
        obj.toDate = binding.tvToDate.text.toString()
        obj.currentDateAndTime = System.currentTimeMillis().toString()
        _viewModel.saveDataToFireBase(obj)
        val bundle = Bundle().apply {
            putParcelableArrayList(AttendanceActivity.KEY_ATTENDANCE, attendanceList)
            putString(AttendanceActivity.KEY_NAME, empName)
        }
        navigator.startActivityWithData(AttendanceActivity::class.java, bundle)
    }


    private fun getDataFromFakeRepository(): ArrayList<AttendanceDto> {
        val fakeList = ArrayList<AttendanceDto>()
        fakeList.add(
            AttendanceDto(
                emp_Id = empId,
                entry_at = "2019-08-01 10:00:56",
                exit_at = "2019-08-01 19:46:47"
            )
        )
        fakeList.add(
            AttendanceDto(
                emp_Id = empId,
                entry_at = "2019-08-02 09:40:33",
                exit_at = "2019-08-02 09:40:33"
            )
        )
        fakeList.add(
            AttendanceDto(
                emp_Id = empId,
                entry_at = "2019-08-03 10:08:12",
                exit_at = "2019-08-03 18:08:55"
            )
        )
        fakeList.add(
            AttendanceDto(
                emp_Id = empId,
                entry_at = "2019-08-16 13:50:47",
                exit_at = "2019-08-16 19:56:23"
            )
        )
        fakeList.add(
            AttendanceDto(
                emp_Id = empId,
                entry_at = "2019-08-17 11:55:16",
                exit_at = null
            )
        )
        return fakeList
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


    private fun observeEmployeeList() {
        _viewModel.setEmployeeList.observe(this) {
            if (it == null || it.isEmpty()) {
                uiUtil.showToast("No Data Found")
                return@observe
            }
            localList.clear()
            employeeHashMap.clear()
            localList.add("Select Employee")
            for (i in it.indices) {
                employeeHashMap[it[i].name.toString()] = it[i].emp_id.toString()
                if (it.isEmpty().not() && it[i].name.isNullOrBlank().not()) {
                    localList.add(it[i].name!!)
                }
            }
            binding.spnrEmployee.adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, localList)
            clearCurrentState()
        }
    }

    private fun clearCurrentState() {
        fromDate = false
        toDate = false
        binding.spnrEmployee.setSelection(0)
        binding.tvFromDate.text = this.getString(R.string.txt_date_format_yyyy_mm_dd)
        binding.tvToDate.text = this.getString(R.string.txt_date_format_yyyy_mm_dd)
    }

    override fun onBackPressed() {
        employeeList.clear()
        super.onBackPressed()
    }


}





