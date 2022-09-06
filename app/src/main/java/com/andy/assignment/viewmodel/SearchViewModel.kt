package com.andy.assignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andy.assignment.EPAHelper
import com.andy.assignment.networking.EPAClient
import com.andy.assignment.views.AirSite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel: ViewModel() {

    val airSites: MutableLiveData<List<AirSite>> = MutableLiveData<List<AirSite>>()
    val showSearchTip: MutableLiveData<Boolean> = MutableLiveData()

    private val mAirSites = mutableListOf<AirSite>()

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    fun getAirPollution() {
        if (!EPAHelper.mAirSites.isEmpty()) {
            airSites.value = EPAHelper.mAirSites
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val res = EPAClient.getAirPollution()
                val records = res.records
                Log.i(TAG, records.toString())

                for (record in records) {
                    record.run {
                        mAirSites.add(AirSite(siteid, sitename, county, pm25, status))
                    }
                }
                EPAHelper.mAirSites = mAirSites
                airSites.value = mAirSites
            } catch (err: Exception) {
                airSites.value = mutableListOf()
            }
        }
    }

}