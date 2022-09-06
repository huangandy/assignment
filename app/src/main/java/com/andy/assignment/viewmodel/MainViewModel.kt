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

class MainViewModel: ViewModel() {

    val hint: MutableLiveData<Pair<FETCH_STATUS, String>> = MutableLiveData()
    val passAirSites: MutableLiveData<List<AirSite>> = MutableLiveData<List<AirSite>>()
    val unPassAirSites: MutableLiveData<List<AirSite>> = MutableLiveData<List<AirSite>>()

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
        private val PM25_THRESHOLD = 16
    }

    fun getAirPollution() {
        hint.value = Pair(FETCH_STATUS.FETCHING, "")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val res = EPAClient.getAirPollution()
                val records = res.records


                for (record in records) {
                    record.run {
                        EPAHelper.mAirSites.add(AirSite(siteid, sitename, county, pm25, status))
                    }
                }
                passAirSites.value = EPAHelper.mAirSites.filter{
                    it.pm2dot5.toIntOrNull()?.run {
                        this > PM25_THRESHOLD
                    } == true
                }
                unPassAirSites.value = EPAHelper.mAirSites.filter{
                        it.pm2dot5.toIntOrNull()?.run {
                        this <= PM25_THRESHOLD
                    } == true || it.pm2dot5.isEmpty()
                }
                hint.value = Pair(FETCH_STATUS.SUCCESS, "")
            } catch (err: Exception) {
                passAirSites.value = mutableListOf()
                unPassAirSites.value = mutableListOf()
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