package com.example.loanapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class RequestDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("REQUEST LOAN")
        builder.setMessage("Experience the Ease of Instant Funding: Request a Loan and Realize Your Ambitions")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        return builder.create()
    }
}
