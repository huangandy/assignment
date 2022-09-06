package com.andy.assignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andy.assignment.networking.EPAClient
import com.andy.assignment.views.AirSite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel: ViewModel() {

    val hint: MutableLiveData<Pair<FETCH_STATUS, String>> = MutableLiveData()
    val airSites: MutableLiveData<List<AirSite>> = MutableLiveData<List<AirSite>>()

    private val mAirSites = mutableListOf<AirSite>()

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    fun getAirPollution() {
        hint.value = Pair(FETCH_STATUS.FETCHING, "")
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

                airSites.value = mAirSites
                hint.value = Pair(FETCH_STATUS.SUCCESS, "")
            } catch (err: Exception) {
                airSites.value = mutableListOf()
                hint.value = Pair(FETCH_STATUS.FAIL, err.message.toString())
            }

        }
    }

    enum class FETCH_STATUS {
        FETCHING,
        SUCCESS,
        FAIL
    }
}