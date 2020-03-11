package com.how_about_now.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity
import com.how_about_now.app.data.DrawerData
import kotlinx.android.synthetic.main.adapter_gifts.view.*


class TelevisionAdapter(
    baseActivity: BaseActivity,
    giftsArrayList: ArrayList<DrawerData>
) :
    RecyclerView.Adapter<TelevisionAdapter.MyViewHolder>() {
    var baseActivity: BaseActivity? = null
    var giftsArrayList: ArrayList<DrawerData>? = null

    init {
        this.baseActivity = baseActivity
        this.giftsArrayList = giftsArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_gifts,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        Glide.with(baseActivity!!).load(giftsArrayList!!.get(position)).into(holder.giftIV)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val giftIV = itemView.giftIV
    }


}
