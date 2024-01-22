package com.example.loanapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class CalculatorDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("LOAN CALCULATOR")
        builder.setMessage("Plan Your Path to Financial Freedom:Calculate Your Loan Options with Confidence")
        builder.setPositiveButton("OK") { dialog, _ ->

            dialog.dismiss()
        }
        return builder.create()
    }
}
