package com.andy.assignment.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andy.assignment.R
import com.andy.assignment.base.BaseActivity
import com.andy.assignment.databinding.ActivityMainBinding
import com.andy.assignment.viewmodel.MainViewModel
import com.andy.assignment.views.AirSite
import com.andy.assignment.views.SiteAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : BaseActivity(), SiteAdapter.OnAdapterEventListener{

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    private var mPassSiteAdapter: SiteAdapter? = null
    private var mUnPassSiteAdapter: SiteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAirPollution()
    }

    override fun initViews() {
        mPassSiteAdapter = SiteAdapter(type = SiteAdapter.TYPE.PASS)
        mBinding.rcvPass.layoutManager = LinearLayoutManager(this)
        mBinding.rcvPass.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        mBinding.rcvPass.adapter = mPassSiteAdapter

        mUnPassSiteAdapter = SiteAdapter(type = SiteAdapter.TYPE.UNPASS)
        mBinding.rcvUnpass.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rcvUnpass.adapter = mUnPassSiteAdapter

        mBinding.btnReload.setOnClickListener({
            mBinding.btnReload.visibility = View.GONE
            mViewModel.getAirPollution();
        })
    }

    override fun initVMObserver() {

        mViewModel.unPassAirSites.observe(this@MainActivity) { airsites ->
            mUnPassSiteAdapter?.run {
                updateList(airsites)
                notifyDataSetChanged()
            }
            mBinding.pbLoading.visibility = View.GONE
        }

        mViewModel.passAirSites.observe(this@MainActivity) { airsites ->
            mPassSiteAdapter?.run {
                setOnAdapterEventListener(this@MainActivity)
                updateList(airsites)
                notifyDataSetChanged()
            }
            mBinding.pbLoading.visibility = View.GONE
        }

        mViewModel.hint.observe(this) { hint ->
            runOnUiThread {
                when(hint.first) {
                    MainViewModel.FETCH_STATUS.FETCHING -> {
                        mBinding.pbLoading.visibility = View.VISIBLE
                        getString(R.string.hint_get_data)
                    }
                    MainViewModel.FETCH_STATUS.SUCCESS -> {
                        ""
                    }
                    MainViewModel.FETCH_STATUS.FAIL -> {
                        mBinding.btnReload.visibility = View.VISIBLE
                        "FAIL: ${hint.second}"
                    }
                    else -> {""}
                }.also {
                    if(!it.isEmpty()) Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        menu?.findItem(R.id.nav_search)?.apply {
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

    override fun onItemClick(){
        Snackbar.make(mBinding.root, "Hello", Snackbar.LENGTH_SHORT).show()
    }

    override fun onSearchList(list: List<AirSite>) {

    }
}