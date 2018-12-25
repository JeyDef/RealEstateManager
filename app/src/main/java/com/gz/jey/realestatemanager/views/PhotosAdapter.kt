package com.gz.jey.realestatemanager.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gz.jey.realestatemanager.models.Data
import com.gz.jey.realestatemanager.models.sql.Photos
import com.gz.jey.realestatemanager.utils.BuildItems
import com.gz.jey.realestatemanager.utils.Utils
import java.util.*

class PhotosAdapter// CONSTRUCTOR
(private val callback: Listener) : RecyclerView.Adapter<PhotosViewHolder>() {

    // FOR DATA
    private lateinit var context: Context
    private var screenX: Int = 0
    private var photos: List<Photos>? = null

    // CALLBACK
    /**
     * INTERFACE FOR ON CLICK DELETE BUTTON
     */
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.photos = ArrayList()
    }

    /**
     * ON CREATE VIEW
     * @param parent ViewGroup
     * @param viewType Int
     * @return PhotosViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        context = parent.context
        val numPic = if (Data.tabMode)
            if (Utils.isLandscape(context)) 6.25f else 5.1f
        else
            if (Utils.isLandscape(context)) 5.1f else 3.15f

        val size = (screenX / numPic).toInt()
        val view = BuildItems().photosCardView(context, size)

        return PhotosViewHolder(view)
    }

    /**
     * ON BIND VIEW
     * @param viewHolder AmortizationsViewHolder
     * @param position Int
     */
    override fun onBindViewHolder(viewHolder: PhotosViewHolder, position: Int) {
        val photo = this.photos!![position]
        viewHolder.updateWithPhotos(photo, this.context, this.callback)
    }

    /**
     * TO GET COUNT OF ITEMS
     * @return Int
     */
    override fun getItemCount(): Int {
        return this.photos!!.size
    }

    /**
     * TO GET ALL PHOTOS
     * @return List<Photos>
     */
    fun getAllPhotos(): List<Photos> {
        return this.photos!!
    }

    /**
     * TO UPDATE DATAS
     * @param photos List<Photos>
     * @param screenX Int
     */
    fun updateData(photos: List<Photos>, screenX: Int) {
        this.photos = photos
        this.screenX = screenX
        this.notifyDataSetChanged()
    }
}