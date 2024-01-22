package com.example.loanapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class RepayDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("REPAY LOAN")
        builder.setMessage("Easy Repayment: Brighter Future Awaits as You Fulfill Your Loan Commitments")
        builder.setPositiveButton("OK") { dialog, _ ->

            dialog.dismiss()
        }
        return builder.create()
    }
}
