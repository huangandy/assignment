package com.andy.assignment.base

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity:  AppCompatActivity() {


    override fun onResume() {
        super.onResume()
        hideStatusBar()
    }

    private fun hideStatusBar() {
        supportActionBar?.hide()
    }

}