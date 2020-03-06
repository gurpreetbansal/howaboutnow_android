package com.how_about_now.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_favourite.view.*


class FavouriteAdapter(
    baseActivity: BaseActivity
) :
    RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {
    var selectedPosition = 0
    var baseActivity: BaseActivity? = null


    init {
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
        return 20
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(baseActivity!!)
            .load("https://source.unsplash.com/Xq1ntWruZQI/600x800")
            .into(holder.profileCIV)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV = itemView.titleTV
        val profileCIV = itemView.profileCIV
    }

    interface DrawerCallBack {
        fun drawerCallBack(postion: Int)
    }

}
