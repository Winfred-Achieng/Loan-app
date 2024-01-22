package com.example.loanapplication

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WalkthroughViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
    private val button: Button = itemView.findViewById(R.id.actionButton)
    private val skipButton: Button = itemView.findViewById(R.id.SkipButton)

    private var buttonClickListener: View.OnClickListener? = null
    private var skipButtonClickListener: View.OnClickListener? = null

    fun bindData(imageResId: Int, title: String, description: String, showButton: Boolean, buttonText: String, clickListener: View.OnClickListener?,skipClickListener: View.OnClickListener?,showSkipButton: Boolean) {
        imageView.setImageResource(imageResId)
        titleTextView.text = title
        descriptionTextView.text = description


        if (showButton) {
            button.visibility = View.VISIBLE
            button.text = buttonText

            buttonClickListener = clickListener
            button.setOnClickListener(buttonClickListener)
        } else {
            button.visibility = View.GONE
        }

        if (showSkipButton) {
            skipButton.visibility = View.VISIBLE
            skipButton.text = "SKIP"

            skipButtonClickListener =skipClickListener
            skipButton.setOnClickListener(skipButtonClickListener)
        } else {
            skipButton.visibility = View.GONE
        }



    }
}
