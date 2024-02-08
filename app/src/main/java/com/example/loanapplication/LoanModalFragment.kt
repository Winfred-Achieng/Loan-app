package com.example.loanapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.loanapplication.databinding.FragmentLoanModalBinding

class LoanModalFragment : DialogFragment() {

    private var _binding: FragmentLoanModalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoanModalBinding.inflate(inflater, container, false)
        val view = binding.root

        val phoneNumberEditText: EditText = binding.editTextPhoneNumber
        val submitButton: Button = binding.buttonSubmit

        // Set click listener for the submit button
        submitButton.setOnClickListener {
            dismiss()
        }


//        cancelButton.setOnClickListener {
//            dismiss() // Close the modal on cancel
//        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
