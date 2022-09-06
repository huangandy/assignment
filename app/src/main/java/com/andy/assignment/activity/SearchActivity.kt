package com.andy.assignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import com.andy.assignment.R
import com.andy.assignment.base.BaseActivity

class SearchActivity : BaseActivity(false, true) {

    companion object {
        private val TAG = SearchActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
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
                    Log.i(TAG, "onQueryTextChange : $newText")
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

}