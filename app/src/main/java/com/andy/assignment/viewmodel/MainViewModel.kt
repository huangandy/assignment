package com.andy.assignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andy.assignment.orm.EPAHelper
import com.andy.assignment.networking.EPAClient
import com.andy.assignment.model.AirSite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel: ViewModel() {

    val hint: MutableLiveData<Pair<FETCH_STATUS, String>> = MutableLiveData() // Use like a visible log
    val passAirSites: MutableLiveData<List<AirSite>> = MutableLiveData<List<AirSite>>() // Sites with pm2.5 larger than PM25_THRESHOLD
    val unPassAirSites: MutableLiveData<List<AirSite>> = MutableLiveData<List<AirSite>>() //Sites with pm2.5 smaller than PM25_THRESHOLD

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
        private var PM25_THRESHOLD = 16
    }

    /**
     * Get PM2.5 Data from EPA
     */
    fun getAirPollution() {
        hint.value = Pair(FETCH_STATUS.FETCHING, "")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val res = EPAClient.getAirPollution()
                val records = res.records

                /*Take all to temp obj*/
                for (record in records) {
                    record.run {
                        EPAHelper.mAirSites.add(AirSite(siteid, sitename, county, pm25, status))
                    }
                }

                updatePM25Threshold() // Demo Only

                /*Take sites with pm2.5 larger than PM25_THRESHOLD*/
                passAirSites.value = EPAHelper.mAirSites.filter{
                    it.pm2dot5.toIntOrNull()?.run {
                        this > PM25_THRESHOLD
                    } == true
                }

                /*Take sites with pm2.5 smaller than PM25_THRESHOLD and Empty pm2.5*/
                unPassAirSites.value = EPAHelper.mAirSites.filter{
                        it.pm2dot5.toIntOrNull()?.run {
                        this <= PM25_THRESHOLD
                    } == true || it.pm2dot5.isEmpty()
                }

                hint.value = Pair(FETCH_STATUS.SUCCESS, "")
            } catch (err: Exception) {
                err.printStackTrace()
                passAirSites.value = mutableListOf()
                unPassAirSites.value = mutableListOf()
                hint.value = Pair(FETCH_STATUS.FAIL, err.message.toString())
            }

        }
    }

    /**
     * Only use for demo
     * dynamic pick the threshold with median for close 50% 50%
     */
    private fun updatePM25Threshold() {
        if (EPAHelper.mAirSites.size < 2) return
        EPAHelper.mAirSites.sortedBy { it.pm2dot5.toIntOrNull()?:100 }.apply {
            PM25_THRESHOLD = if (size % 2 == 1)
                 get((size - 1 )/2).pm2dot5.toIntOrNull()?:16 else
                (get((size / 2)-1).pm2dot5.toIntOrNull()?:30*0.5).toInt()
        }
    }

    /**
     * Simple status
     */
    enum class FETCH_STATUS {
        FETCHING,
        SUCCESS,
        FAIL
    }
}