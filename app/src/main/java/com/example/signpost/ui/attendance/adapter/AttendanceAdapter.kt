package com.app.ilmainstitute.ui.teacher.admission_request

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ilmainstitute.databinding.RvAdmsnRequestListBinding
import com.app.ilmainstitute.ui.admission.AdmissionFormModel

class AttendanceAdapter :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    private val items = ArrayList<AttendanceDto>()

    fun submitList(studentList:AttendanceDto) {
        items.clear()
        notifyDataSetChanged()
    }

    inner class AttendanceViewHolder(private val binding: IndiviewAttendanceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AttendanceDto) {
            binding.tvDate.text = getWeekday(item.entryAt)
            binding.tvTime.text = getNoOfHours(item.entryAt,item.exitAt)
        }

    }

    private fun getNoOfHours(entrydate:String,exitDate:String):String{
        return ""
    }
    private fun getWeekday(entrydate:String):String{
        return ""
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding =
            IndiviewAttendanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

}