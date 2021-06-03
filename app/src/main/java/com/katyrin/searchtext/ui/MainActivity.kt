package com.katyrin.searchtext.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.katyrin.searchtext.App
import com.katyrin.searchtext.R
import com.katyrin.searchtext.databinding.ActivityMainBinding
import com.katyrin.searchtext.di.AppComponent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = App.appInstance.appComponent
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