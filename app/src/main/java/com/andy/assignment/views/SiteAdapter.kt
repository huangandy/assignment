package com.andy.assignment.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.andy.assignment.R

class SiteAdapter(private val type:TYPE = TYPE.ALL): RecyclerView.Adapter<SiteAdapter.ViewHolder>(), Filterable {

    private var airSites: MutableList<AirSite> = mutableListOf()
    private var mFilteredAirSites: MutableList<AirSite> = mutableListOf()
    private var mListener: OnAdapterEventListener? = null

    enum class TYPE {
        ALL,
        PASS,
        UNPASS
    }

    fun updateList(airSites: List<AirSite>) {
        this.airSites.addAll(airSites)
        this.mFilteredAirSites.addAll(airSites)
    }

    fun setOnAdapterEventListener(listener: OnAdapterEventListener) {
        this.mListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txv_id : TextView
        val txv_name : TextView
        val txv_county : TextView
        val txv_pm2dot5 : TextView
        val txv_status : TextView
        val img_more : ImageView
        val root: ConstraintLayout

        init {
            root = view.findViewById(R.id.item_pass)
            txv_id = view.findViewById(R.id.item_txv_site_id)
            txv_name = view.findViewById(R.id.item_txv_site_name)
            txv_county = view.findViewById(R.id.item_txv_site_county)
            txv_pm2dot5 = view.findViewById(R.id.item_txv_site_pm2dot5)
            txv_status = view.findViewById(R.id.item_txv_site_status)
            img_more = view.findViewById(R.id.item_img_site_more)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when(type) {
            TYPE.PASS,TYPE.ALL -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pass_site, parent, false)
            TYPE.UNPASS -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_unpass_site, parent, false)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            txv_id.text = airSites[position].siteId
            txv_name.text = airSites[position].siteName
            txv_county.text = airSites[position].county
            txv_pm2dot5.text = airSites[position].pm2dot5

            when (type) {
                TYPE.PASS, TYPE.ALL -> {
                    txv_status.text = if(airSites[position].status.equals("良好")) "The status is good, we want to go out to have fun." else airSites[position].status
                    img_more.visibility = if(!airSites[position].status.equals("良好")) View.VISIBLE else View.GONE
                    root.setOnClickListener {
                        if(!airSites[position].status.equals("良好")) this@SiteAdapter.mListener?.onItemClick()
                    }
                }
                TYPE.UNPASS -> {
                    txv_status.text = airSites[position].status
                }
            }

        }
    }

    override fun getItemCount() = airSites.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charString = constraint?.toString() ?: ""
                var filterResults = FilterResults()
                if (charString.isEmpty()) {
                    filterResults.apply {
                        count = mFilteredAirSites.size
                        values = mFilteredAirSites
                    }
                } else {
                    val filteredList = mFilteredAirSites.filter {
                            it.siteName.contains(charString)
                        }
                    filterResults.apply {
                        count = filteredList.size
                        values = filteredList
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                airSites = results?.values as MutableList<AirSite>
                notifyDataSetChanged()
                this@SiteAdapter.mListener?.onSearchList(airSites)
            }
        }
    }

    interface OnAdapterEventListener {
        fun onItemClick()
        fun onSearchList(list: List<AirSite>)
    }

}

