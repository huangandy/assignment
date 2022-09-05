package com.andy.assignment.base

import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity(showBarTitle: Boolean = true, showBarBack: Boolean = false):  AppCompatActivity() {

    val mShowBarTitle = showBarTitle
    val mShowBarBack = showBarBack

    override fun onResume() {
        super.onResume()
        if (!mShowBarTitle) supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(mShowBarBack)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}