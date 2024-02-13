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
    private var userName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        arguments?.let {
            userName = it.getString("user_name")
        }

        if (userName != null && _binding != null) {
            _binding!!.tvUsername.text = "Welcome Back, $userName!"
        }


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





    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Nullify the binding to avoid memory leaks
        _binding = null
    }
}
