package com.gz.jey.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gz.jey.realestatemanager.R
import com.gz.jey.realestatemanager.controllers.activities.MainActivity
import com.gz.jey.realestatemanager.models.Photos
import com.gz.jey.realestatemanager.models.RealEstate
import com.gz.jey.realestatemanager.utils.ItemClickSupport
import com.gz.jey.realestatemanager.views.RealEstateAdapter

class RealEstateList : Fragment(), RealEstateAdapter.Listener {
    private var mView: View? = null

    var mainActivity: MainActivity? = null
    private var adapter: RealEstateAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var infoText: TextView? = null
    private var itemPos: Int? = null
    private var slct: Int = 0
    private var maxClick = 1


    /**
     * CALLED ON INSTANCE OF THIS FRAGMENT TO CREATE VIEW
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.real_estate_list, container, false)
        mainActivity = activity as MainActivity
        maxClick = if (mainActivity!!.tabLand) 1 else 2
        return mView
    }

    /**
     * CALLED WHEN VIEW CREATED
     * @param view View
     * @param savedInstanceState Bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.infoText = view.findViewById(R.id.re_text)
        this.recyclerView = view.findViewById(R.id.real_estate_recycler_view)
        configureRecyclerView()
        init()
    }

    override fun onClickDeleteButton(position: Int) {
        // DELETE
    }


    private fun configureRecyclerView() {
        this.adapter = RealEstateAdapter(this.context!!, this)
        this.recyclerView!!.adapter = this.adapter
        this.recyclerView!!.layoutManager = LinearLayoutManager(this.context)
        ItemClickSupport.addTo(recyclerView, R.layout.real_estate_item)
                .setOnItemClickListener { _, position, _ ->
                    if (itemPos != position && slct != maxClick) {
                        itemPos = position
                        this.updateRealEstate(this.adapter!!.getRealEstate(position), this.adapter!!.getAllRealEstate())
                    }
                }
    }

    private fun init() {
        Log.d("RE LIST", "OK")
        getRealEstates()
    }

    private fun getRealEstates() {
        mainActivity!!
                .realEstateViewModel
                .getAllRealEstate()
                .observe(this, Observer<List<RealEstate>> { re -> updateRealEstateList(re!!) })
    }

    private fun updateRealEstate(realEstate: RealEstate, realEstates: List<RealEstate>) {
        realEstate.isSelected = true

        for (r in realEstates) {
            if (r != realEstate) {
                r.isSelected = false
            }
            mainActivity!!.realEstateViewModel.updateRealEstate(r)
        }

        if(mainActivity!!.realEstate == realEstate && !mainActivity!!.tabLand){
            slct = 2
        }else{
            mainActivity!!.setRE(realEstate)
            slct++
        }


        if (slct == maxClick ) {
            mainActivity!!.setRE(realEstate)
            slct = 0
            if (mainActivity!!.tabLand){
                mainActivity!!.realEstateDetails!!.init()
            }
            else{
                realEstate.isSelected = false
                mainActivity!!.realEstateViewModel.updateRealEstate(realEstate)
                mainActivity!!.setFragment(1)
            }
        }
    }

    private fun updateRealEstateList(items: List<RealEstate>) {
        Log.d("RE LIST", items.toString())
        if (items.isEmpty())
            infoText!!.text = "No Real Estate found !"
        else
            infoText!!.visibility = View.GONE

        val photos: ArrayList<Photos?> = ArrayList()
        for ((i, it) in items.withIndex()) {
            mainActivity!!.realEstateViewModel.getAllPhotos(it.id!!).observe(this, Observer<List<Photos>> { p ->
                if (p!!.isNotEmpty()) photos.add(p[0])
                else {
                    val ph = Photos(null, null, null, null)
                    photos.add(ph)
                }

                if (i >= items.size - 1)
                    this.adapter!!.updateData(items, photos as List<Photos>)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mainActivity = null
    }

    companion object {
        /**
         * @param mainActivity MainActivity
         * @return new RealEstateList()
         */
        fun newInstance(mainActivity: MainActivity): RealEstateList {
            val fragment = RealEstateList()
            fragment.mainActivity = mainActivity
            return fragment
        }
    }
}