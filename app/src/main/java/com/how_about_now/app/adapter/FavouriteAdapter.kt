package com.how_about_now.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity
import com.how_about_now.app.data.favoirite_phase.FavouriteMsg
import kotlinx.android.synthetic.main.adapter_favourite.view.*


class FavouriteAdapter(
    baseActivity: BaseActivity,
    favouriteArrayList: ArrayList<FavouriteMsg>
) :
    RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {
    var selectedPosition = 0
    var baseActivity: BaseActivity? = null
    var favouriteArrayList: ArrayList<FavouriteMsg>? = null


    init {
        this.favouriteArrayList = favouriteArrayList
        this.baseActivity = baseActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_favourite,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return favouriteArrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var favouriteMsg = favouriteArrayList!!.get(position)

        holder.titleTV.setText(favouriteMsg.name)
        Glide.with(baseActivity!!)
            .load(favouriteMsg.image)
            .into(holder.profileCIV)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV = itemView.titleTV
        val profileCIV = itemView.profileCIV
    }

    interface DrawerCallBack {
        fun drawerCallBack(position: Int)
    }

}
