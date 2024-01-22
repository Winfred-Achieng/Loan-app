package com.example.loanapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class StatusDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("LOAN STATUS")
        builder.setMessage("Stay in the Know: Track Your Loan Progress and Stay ahead in Your Financial Journey.")
        builder.setPositiveButton("OK") { dialog, _ ->

            dialog.dismiss()
        }
        return builder.create()
    }
}
