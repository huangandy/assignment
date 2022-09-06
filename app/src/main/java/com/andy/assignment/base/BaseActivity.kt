package com.andy.assignment.base

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * Do the common things, ex: permission, styles or something
 * @param showBarBack will show top left back button
 * @param showBarTitle will show app name
 */
open abstract class BaseActivity(showBarTitle: Boolean = true, showBarBack: Boolean = false):  AppCompatActivity() {

    val mShowBarTitle = showBarTitle
    val mShowBarBack = showBarBack

    /**
     * initial view with basic property
     * will be call in onCreate
     */
    abstract fun initViews()

    /**
     * initial observer with VM lifedata
     * will be call in onCreate after initView
     */
    abstract fun initVMObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initVMObserver()
    }

    override fun onResume() {
        super.onResume()
        if (!mShowBarTitle) supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(mShowBarBack)
    }

    /**
     * Trigger when back button is shown
     */
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