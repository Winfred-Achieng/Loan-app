package com.example.loanapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.loanapplication.viewModel.AuthViewModel
import com.example.loanapplication.databinding.ActivityWalkthroughBinding

class WalkthroughActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalkthroughBinding
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getStartedLastScreenClickListener = View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val skipButtonClickListener = View.OnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val viewPager: ViewPager2 = binding.viewPager
        val adapter = WalkthroughPagerAdapter(
            listOf(
                R.layout.walkthrough_screen_1,
                R.layout.walkthrough_screen_2,
                R.layout.walkthrough_screen_3
            ),
            getStartedButtonClickListener,
            skipButtonClickListener,
            getStartedLastScreenClickListener

        )
        viewPager.adapter = adapter
    }

    private val getStartedButtonClickListener = View.OnClickListener {
        val currentPosition = binding.viewPager.currentItem
        val totalScreens = binding.viewPager.adapter?.itemCount ?: 0

        if (currentPosition < totalScreens -1){
            binding.viewPager.setCurrentItem(currentPosition + 1, true)
        }
        else{
            navigateToActivityForLastScreen()
        }

    }
    private fun navigateToActivityForLastScreen() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }


}

