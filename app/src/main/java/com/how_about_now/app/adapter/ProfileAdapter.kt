package com.how_about_now.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity
import kotlinx.android.synthetic.main.adapter_profile.view.*


class ProfileAdapter(
    baseActivity: BaseActivity
) :
    RecyclerView.Adapter<ProfileAdapter.MyViewHolder>() {
    var selectedPosition = 0
    var baseActivity: BaseActivity? = null


    init {
        this.baseActivity = baseActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_profile,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//        Glide.with(baseActivity!!)
//            .load("https://source.unsplash.com/Xq1ntWruZQI/600x800")
//            .into(holder.profileCIV)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileCIV = itemView.profileCIV
    }

    interface DrawerCallBack {
        fun drawerCallBack(position: Int)
    }

}
