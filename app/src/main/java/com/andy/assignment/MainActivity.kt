package com.andy.assignment

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.andy.assignment.activity.SearchActivity
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        val search = menu?.findItem(R.id.nav_search)?.apply {
            actionView = null
            setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { menuItem ->
                when(itemId){
                    R.id.nav_search -> {
                        startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                    }
                }
                true
            })
        }

        return super.onCreateOptionsMenu(menu)
    }
}