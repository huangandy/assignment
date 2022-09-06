package com.andy.assignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andy.assignment.EPAHelper
import com.andy.assignment.R
import com.andy.assignment.base.BaseActivity
import com.andy.assignment.databinding.ActivityMainBinding
import com.andy.assignment.databinding.ActivitySearchBinding
import com.andy.assignment.viewmodel.MainViewModel
import com.andy.assignment.viewmodel.SearchViewModel
import com.andy.assignment.views.AirSite
import com.andy.assignment.views.SiteAdapter

class SearchActivity : BaseActivity(false, true), SiteAdapter.OnAdapterEventListener {

    companion object {
        private val TAG = SearchActivity::class.java.simpleName
    }

    private lateinit var mBinding: ActivitySearchBinding
    private lateinit var mViewModel: SearchViewModel
    private var mSiteAdapter: SiteAdapter? = null
    private var mCurrentSearchText: String? = null

    override fun initViews() {
        mSiteAdapter = SiteAdapter()
        mBinding.rcvSite.layoutManager = LinearLayoutManager(this)
        mBinding.rcvSite.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        mBinding.rcvSite.adapter = mSiteAdapter
    }

    override fun initVMObserver() {
        mViewModel.airSites.observe(this@SearchActivity) { airsites ->
            mSiteAdapter?.run {
                setOnAdapterEventListener(this@SearchActivity)
                updateList(airsites)
                filter?.filter("")
                notifyDataSetChanged()
            }
            mBinding.searchPbLoading.visibility = View.GONE
        }

        mViewModel.showSearchTip.observe(this@SearchActivity) { show ->
            if(show) {
                mBinding.txvListHint.text = getString(R.string.search_input_hint)
                mBinding.txvListHint.visibility = View.VISIBLE

            } else {
                mBinding.txvListHint.visibility = View.GONE
                mBinding.txvListHint.text = ""
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAirPollution()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        val search = menu?.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView

        searchView.apply {
            setIconifiedByDefault(false)
            queryHint = context.getString(R.string.search_input_hint)
            setFocused(this)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.i(TAG, "onQueryTextChange $newText")
                    mCurrentSearchText = newText
                    mSiteAdapter?.filter?.filter(newText)
                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun setFocused(searchView: SearchView) {
        searchView.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hasInput(): Boolean = !mCurrentSearchText.isNullOrEmpty()

    override fun onItemClick(item: AirSite) {

    }

    override fun onSearchList(list: List<AirSite>) {
        runOnUiThread {

            mBinding.rcvSite.apply {
                visibility = if(hasInput()) View.VISIBLE else View.GONE
            }

            mBinding.txvListHint.apply {
                if (list.isEmpty()) {
                    text = getString(com.andy.assignment.R.string.search_list_empty, mCurrentSearchText)
                    visibility = View.VISIBLE
                } else if (!hasInput()) {
                    text = getString(R.string.search_list_hint)
                    visibility = android.view.View.VISIBLE
                } else {
                    text = ""
                    visibility = View.GONE
                }
            }
        }
    }

}