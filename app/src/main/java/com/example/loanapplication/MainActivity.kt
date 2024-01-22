package com.example.loanapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.loanapplication.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.loanapplication.CustomTypefaceSpan

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var topAppBar : MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Dashboard())

        topAppBar = binding.topAppBar
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.dashboard -> replaceFragment(Dashboard())
                R.id.request -> replaceFragment(Request())
                R.id.repay -> replaceFragment(Repay())
                R.id.status -> replaceFragment(Status())
                R.id.history -> replaceFragment(History())
                else -> {}
            }
            true
        }

        applyCustomFontToBottomNavTitles()
    }

    private fun applyCustomFontToBottomNavTitles() {
        val menu = binding.bottomNavigationView.menu

        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val spannable = SpannableString(menuItem.title)

            spannable.setSpan(
                AbsoluteSizeSpan(12, true),
                0,
                spannable.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

            val customFontBottomNav = ResourcesCompat.getFont(this, R.font.inter)
            if (customFontBottomNav != null) {
                spannable.setSpan(
                    CustomTypefaceSpan(customFontBottomNav),
                    0,
                    spannable.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                menuItem.title = spannable
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
