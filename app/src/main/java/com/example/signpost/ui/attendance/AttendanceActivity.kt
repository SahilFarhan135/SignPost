package com.example.signpost.ui.attendance

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkdomain.model.AttendanceDto
import com.example.signpost.R
import com.example.signpost.base.BaseActivity
import com.example.signpost.base.ViewState
import com.example.signpost.databinding.ActivityAttendanceBinding
import com.example.signpost.ui.attendance.adapter.AttendanceAdapter
import com.example.signpost.util.findDifference
import com.example.signpost.util.findNoOfDaysBetweenDates
import com.example.signpost.util.getMonthInString
import com.example.signpost.util.isWeekend
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AttendanceActivity : BaseActivity<ActivityAttendanceBinding>() {

    override fun layoutId(): Int = R.layout.activity_attendance
    private var list = ArrayList<AttendanceDto>()
    var hm = HashMap<String, String>()
    var noOfDays = "0"

    private val _viewModel: AttendanceViewModel by viewModels()

    companion object {
        const val KEY_ATTENDANCE = "KEY_ATTENDANCE"
        const val KEY_NAME = "KEY_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
        initUi()
        addListener()
        addObservers()
    }



    @SuppressLint("SetTextI18n")
    private fun getData() {
        if (intent.getParcelableArrayListExtra<AttendanceDto>(KEY_ATTENDANCE) != null) {
            list =
                intent.getParcelableArrayListExtra<AttendanceDto>(KEY_ATTENDANCE) as ArrayList<AttendanceDto>
        }
        if (intent.getStringExtra(KEY_NAME).isNullOrBlank().not()) {
            binding.appbar.tvTitle.text =
                "${intent.getStringExtra(KEY_NAME).toString()} 's Attendance Report"

        }
    }

    private fun initUi() {
        binding.appbar.btBack.visibility = View.VISIBLE
        list.forEach {
            if(it.entry_at.isNullOrBlank().not()||it.entry_at.equals("null",true).not()){
                binding.tvDate.text=it.entry_at?.getMonthInString()
                return@forEach
            }
        }
        binding.rvAttendance.layoutManager = LinearLayoutManager(this)
        binding.rvAttendance.adapter = AttendanceAdapter()
        if(list.isNullOrEmpty().not()){
            (binding.rvAttendance.adapter as AttendanceAdapter).submitList(list)
            binding.tvPresent.text = getTotalNoOfPresent()
            binding.tvLoggedHours.text = getTotalHoursLogged()
            binding.tvAbsent.text = getTotalNoOfDaysAbsent()
        }
    }
    private fun getTotalHoursLogged(): String {
        var count = 0
        list.forEach {
            count += findDifference(it.entry_at.toString(), it.exit_at.toString()).toIntOrNull()
                ?: 0
        }
        return count.toString()
    }

    private fun getTotalNoOfPresent(): String {
        var firstDay = ""
        var lastDay = ""
        if (list[0].entry_at.isNullOrBlank().not() && list[0].entry_at.equals("null", true).not()) {
            firstDay = list[0].entry_at.toString()
        } else {
            list.forEach {
                if (it.entry_at.isNullOrBlank().not() && it.entry_at.equals("null", true).not()) {
                    firstDay = it.entry_at.toString()
                    return@forEach
                }
            }
        }
        if (list.last().entry_at.isNullOrBlank().not() && list.last().entry_at.equals("null", true)
                .not()
        ) {
            lastDay = list.last().entry_at.toString()
        } else {
            val localList = list.clone() as ArrayList<AttendanceDto>
            localList.asReversed().forEach { item ->
                if (item.entry_at.isNullOrBlank().not() && item.entry_at.equals("null", true)
                        .not()
                ) {
                    firstDay = item.entry_at.toString()
                    return@forEach
                }
            }
        }
        noOfDays = findNoOfDaysBetweenDates(firstDay, lastDay)
        var holiday = 0
        list.forEach {
            if (it.entry_at.toString().isWeekend()) {
                holiday++
            }
        }
        return (list.size - holiday).toString()
    }

    private fun getTotalNoOfDaysAbsent(): String {
        return ((noOfDays.toIntOrNull() ?: 30) - list.size).toString()
    }

    private fun addListener() {
        binding.appbar.btBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun addObservers() {
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
/*
        _viewModel.setResponseCodeForAttendanceList.observe(this) {
            showToast(it.toString())
        }
*/
    }


}





