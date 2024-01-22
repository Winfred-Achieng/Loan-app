package com.example.loanapplication

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loanapplication.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.ArrayList
import java.util.Arrays

class Dashboard : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var lineChart: LineChart
    private lateinit var xValues: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardView1.setOnClickListener{
            val dialogFragment = RequestDialogFragment()
            dialogFragment.show(parentFragmentManager, "RequestLoanTag")
        }
        binding.cardView2.setOnClickListener{
            val dialogFragment = RepayDialogFragment()
            dialogFragment.show(parentFragmentManager, "RepayLoanTag")
        }
        binding.cardView3.setOnClickListener{
            val dialogFragment = StatusDialogFragment()
            dialogFragment.show(parentFragmentManager, "StatusLoanTag")
        }
        binding.cardView4.setOnClickListener{
            val dialogFragment = CalculatorDialogFragment()
            dialogFragment.show(parentFragmentManager, "CalculatorLoanTag")
        }





       // lineChart = binding.lineChart

//        val description = Description()
//        description.text = ""
//        description.setPosition(150f, 15f)
//        lineChart.description = description
//        lineChart.axisRight.setDrawLabels(false)
//
//        xValues = Arrays.asList("JAN", "FEB", "MAR", "APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC",)
//
//        val xAxis = lineChart.xAxis
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
//        xAxis.labelCount = xValues.size
//        xAxis.granularity = 1f
//
//        val yAxis = lineChart.axisLeft
//        yAxis.axisMinimum = 0f
//        yAxis.axisMaximum = 100f
//        yAxis.axisLineWidth = 2f
//        yAxis.axisLineColor = Color.BLACK
//        yAxis.labelCount = 10
//        yAxis.setDrawGridLines(false)
//        yAxis.setDrawGridLinesBehindData(false)
//
//        val entries1 = ArrayList<Entry>()
//        entries1.add(Entry(0f, 10f))
//        entries1.add(Entry(1f, 10f))
//        entries1.add(Entry(2f, 15f))
//        entries1.add(Entry(3f, 45f))
//        entries1.add(Entry(4f, 48f))
//        entries1.add(Entry(5f, 36f))
//        entries1.add(Entry(6f, 58f))
//        entries1.add(Entry(7f, 62f))
//        entries1.add(Entry(8f, 55f))
//        entries1.add(Entry(9f, 85f))
//        entries1.add(Entry(10f, 74f))
//        entries1.add(Entry(11f, 95f))
//
//        val entries2 = ArrayList<Entry>()
//        entries2.add(Entry(0f, 5f))
//        entries2.add(Entry(1f, 15f))
//        entries2.add(Entry(2f, 25f))
//        entries2.add(Entry(3f, 30f))
//        entries2.add(Entry(4f, 48f))
//        entries2.add(Entry(5f, 34f))
//        entries2.add(Entry(6f, 53f))
//        entries2.add(Entry(7f, 68f))
//        entries2.add(Entry(8f, 54f))
//        entries2.add(Entry(9f, 81f))
//        entries2.add(Entry(10f, 77f))
//        entries2.add(Entry(11f, 99f))
//
//        val dataSet1 = LineDataSet(entries1, "")
//        dataSet1.color = Color.BLUE
//        dataSet1.setDrawValues(false)
//
//        val dataSet2 = LineDataSet(entries2, "")
//        dataSet2.color = Color.GREEN
//        dataSet1.setDrawValues(false)
//
//        val lineData = LineData(dataSet1, dataSet2)
//        lineChart.data = lineData
//
//        lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Nullify the binding to avoid memory leaks
        _binding = null
    }
}
