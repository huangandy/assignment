package com.andy.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.andy.assignment.base.BaseActivity
import com.andy.assignment.databinding.ActivityMainBinding
import com.andy.assignment.networking.EPAClient
import com.andy.assignment.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initVMObserver()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAirPollution()
    }

    private fun initVMObserver() {

    }
}