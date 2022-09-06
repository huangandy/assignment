package com.andy.assignment.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andy.assignment.orm.EPAHelper
import com.andy.assignment.R
import com.andy.assignment.base.BaseActivity
import com.andy.assignment.databinding.ActivityMainBinding
import com.andy.assignment.viewmodel.MainViewModel
import com.andy.assignment.model.AirSite
import com.andy.assignment.views.SiteAdapter
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar

class MainActivity : BaseActivity(), SiteAdapter.OnAdapterEventListener {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    private var mPassSiteAdapter: SiteAdapter? = null
    private var mUnPassSiteAdapter: SiteAdapter? = null
    private lateinit var mUnPassSkeleton: Skeleton
    private lateinit var mPassSkeleton: Skeleton

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
        mPassSkeleton = mBinding.rcvPass.applySkeleton(R.layout.item_pass_site, 10)

        mBinding.rcvPassSwipe.setOnRefreshListener {
            mViewModel.getAirPollution()
        }

        mUnPassSiteAdapter = SiteAdapter(type = SiteAdapter.TYPE.UNPASS)
        mBinding.rcvUnpass.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rcvUnpass.adapter = mUnPassSiteAdapter
        mUnPassSkeleton = mBinding.rcvUnpass.applySkeleton(R.layout.item_unpass_site)

        mBinding.btnReload.setOnClickListener({
            mBinding.btnReload.visibility = View.GONE
            mViewModel.getAirPollution();
        })
    }

    override fun initVMObserver() {

        mViewModel.unPassAirSites.observe(this@MainActivity) { airsites ->
            mUnPassSkeleton.showOriginal()
            mUnPassSiteAdapter?.run {
                updateList(airsites)
                notifyDataSetChanged()
            }
        }

        mViewModel.passAirSites.observe(this@MainActivity) { airsites ->
            mBinding.rcvPassSwipe.isRefreshing = false;
            mPassSkeleton.showOriginal()
            mPassSiteAdapter?.run {
                setOnAdapterEventListener(this@MainActivity)
                updateList(airsites)
                notifyDataSetChanged()
            }
        }

        mViewModel.hint.observe(this) { hint ->
            runOnUiThread {
                when(hint.first) {
                    MainViewModel.FETCH_STATUS.FETCHING -> {
                        mUnPassSkeleton.showSkeleton()
                        mPassSkeleton.showSkeleton()
                        mBinding.rcvPassSwipe.isRefreshing = true
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
                        if (!EPAHelper.mAirSites.isEmpty()) {
                            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                        } else {
                            Snackbar.make(mBinding.root, getString(R.string.main_retry_hint), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                true
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemClick(item: AirSite){
        Snackbar.make(mBinding.root, item.siteName, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSearchList(list: List<AirSite>) {

    }

}