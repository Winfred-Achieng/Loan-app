package com.example.loanapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WalkthroughPagerAdapter(private val layouts: List<Int>,
                              private val buttonClickListener: View.OnClickListener,
                              private val skipButtonClickListener: View.OnClickListener,
                              private val lastScreenClickListener: View.OnClickListener)
    : RecyclerView.Adapter<WalkthroughViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkthroughViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layouts[viewType], parent, false)
        return WalkthroughViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalkthroughViewHolder, position: Int) {
        when (position) {
            0 -> holder.bindData(R.drawable.walkthrough1, "UNLOCK FINANCIAL FREEDOM", "Seize financial freedom and unlock the lifestyle you've dreamt of.", true, "Next", buttonClickListener,skipButtonClickListener,true)
            1 -> holder.bindData(R.drawable.walkthrough2, "TRACK YOUR LOAN JOURNEY", "Stay empowered and in control on your personalized financial journey.", true, "Next", buttonClickListener,null,false)
            2 -> holder.bindData(R.drawable.walkthrough3, "SMART FINANCIAL PLANNING", "Witness your financial story unfold with precision, offering insights for informed decisions.", true, "GET STARTED", lastScreenClickListener,null,false)
        }
    }


    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
