package com.example.loanapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.d("MenuItem","Item clicked: ${item.title}")
//        when (item.itemId) {
//            R.id.logout -> {
//                logout()
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//
//    }

    fun logoutOfMain(item: MenuItem) {
       logout()
        Toast.makeText(this, "Logged out ", Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        Log.d("Logout", "Before starting LoginActivity")
        supportFragmentManager.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        Log.d("Logout", "After starting LoginActivity")

        finish()
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

        if (fragment is Dashboard){
            val bundle = Bundle()
            val userName = intent.getStringExtra("user_name")
            bundle.putString("user_name", userName)
            fragment.arguments = bundle
        }

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
