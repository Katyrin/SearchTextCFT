package com.katyrin.searchtext.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.katyrin.searchtext.R
import com.katyrin.searchtext.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) replaceMainFragment()
    }

    private fun replaceMainFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
    }
}