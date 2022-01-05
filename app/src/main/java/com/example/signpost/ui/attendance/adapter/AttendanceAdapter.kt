package com.example.signpost.ui.attendance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.networkdomain.model.AttendanceDto
import com.example.signpost.databinding.IndiviewAttendanceItemBinding
import com.example.signpost.util.findDifference
import java.util.*
import kotlin.collections.ArrayList

class AttendanceAdapter :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    private val items = ArrayList<AttendanceDto>()
    val c: Calendar = Calendar.getInstance()

    var strDays =
        arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    fun submitList(attendance: ArrayList<AttendanceDto>) {
        items.clear()
        items.addAll(attendance)
        notifyDataSetChanged()
    }

    inner class AttendanceViewHolder(private val binding: IndiviewAttendanceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AttendanceDto) {
            val local = item.entry_at?.split("-")
            if (local.isNullOrEmpty().not() && local?.get(2).isNullOrEmpty().not()) {
                binding.tvDate.text = local?.get(2) ?: "---"
            }
            val dif = findDifference(item.entry_at.toString(), item.exit_at.toString())
            binding.tvTime.text = if (dif.equals("0", true)) "Weekend" else dif
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding =
            IndiviewAttendanceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}