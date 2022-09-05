package com.andy.assignment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.andy.assignment.networking.EPAClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    fun getAirPollution() {
        CoroutineScope(Dispatchers.Main).launch {
            val res = EPAClient.getAirPollution()
            val records = res.records
            Log.i(TAG, records.toString())
        }
    }
}