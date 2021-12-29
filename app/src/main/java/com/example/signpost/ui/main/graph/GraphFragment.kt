package com.example.signpost.ui.main.graph

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.signpost.R
import com.example.signpost.base.BaseFragment
import com.example.signpost.databinding.FragmentGraphBinding
import com.example.signpost.ui.main.MainViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraphFragment : BaseFragment<FragmentGraphBinding>() {

    val xAxis = ArrayList<Int>()
    val yAxis = ArrayList<String>()
    val mainList = ArrayList<Dummy>()
    override fun getLayout(): Int = R.layout.fragment_graph
    private val _viewModel: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 1..20) {
            if (i % 2 == 0) {
                xAxis.add(i + 89)
            } else xAxis.add(i + 39)

            yAxis.add((11 + i).toString())
        }
        for (i in xAxis.indices) {
            mainList.add(Dummy(xAxis.get(i), yAxis[i]))
        }
        initLineChart()
        setDataToLineChart()
    }

    override fun addObservers() {

    }


    private fun initLineChart() {

//        hide grid lines
        binding.activityMainLinechart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = binding.activityMainLinechart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        binding.activityMainLinechart.axisRight.isEnabled = false

        //remove legend
        binding.activityMainLinechart.legend.isEnabled = false


        //remove description label
        binding.activityMainLinechart.description.isEnabled = false


        //add animation
        binding.activityMainLinechart.animateX(1000, Easing.EaseInSine)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < mainList.size) {
                mainList[index].label
            } else {
                ""
            }
        }
    }

    private fun setDataToLineChart() {
        val entries: ArrayList<Entry> = ArrayList()


        //you can replace this data object with  your custom object
        for (i in mainList.indices) {
            val score = mainList[i]
            entries.add(Entry(i.toFloat(), score.value.toFloat()))
        }
        val lineDataSet = LineDataSet(entries, "")
        val data = LineData(lineDataSet)
        binding.activityMainLinechart.data = data
        binding.activityMainLinechart.invalidate()
    }
}

data class Dummy(val value: Int, val label: String)