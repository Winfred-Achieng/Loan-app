package com.example.loanapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loanapplication.databinding.FragmentDashboardBinding
import com.example.loanapplication.databinding.FragmentRequestBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Request : Fragment() {

    private var _binding:FragmentRequestBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRequestBinding.inflate(inflater, container, false)


        val fabRequestLoan = binding.root.findViewById<FloatingActionButton>(R.id.fabRequestLoan)

        fabRequestLoan.setOnClickListener {

            showLoanRequestModal()
        }

        return binding.root
    }

    private fun showLoanRequestModal() {
        val modalFragment = LoanModalFragment()
        modalFragment.show(parentFragmentManager, "LoanModalFragment")
    }



}