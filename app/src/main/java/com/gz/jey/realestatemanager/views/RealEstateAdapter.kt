package com.gz.jey.realestatemanager.views

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.injection.RealEstateViewModel
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.utils.SetImageColor
import java.util.*
import kotlin.collections.ArrayList

class RealEstateAdapter// CONSTRUCTOR
(private val callback: Listener) : RecyclerView.Adapter<RealEstateViewHolder>() {

    // FOR DATA
    private lateinit var context : Context
    private var realEstates: List<RealEstate>
    private var photos: List<Photos>

    // CALLBACK
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.realEstates = ArrayList()
        this.photos = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.real_estate_item, parent, false)

        return RealEstateViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RealEstateViewHolder, position: Int) {
        Log.d("RE ADAPTER", "POSITION = $position")
        val re = this.realEstates[position]
        val ph : Photos? = if(this.photos.isNotEmpty())this.photos[position]else null
        viewHolder.updateWithRealEstate(re, ph, this.callback)
    }

    override fun getItemCount(): Int {
        return this.realEstates.size
    }

    fun getAllRealEstate(): List<RealEstate> {
        return this.realEstates
    }

    fun getRealEstate(position: Int): RealEstate {
        return this.realEstates[position]
    }

    fun updateData(realEstates: List<RealEstate>, photos : List<Photos>) {
        this.realEstates = realEstates
        this.photos = photos
        this.notifyDataSetChanged()
    }
}