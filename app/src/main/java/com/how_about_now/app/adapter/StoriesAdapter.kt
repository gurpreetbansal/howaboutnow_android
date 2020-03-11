package com.how_about_now.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity
import kotlinx.android.synthetic.main.adapter_drawer.view.*


class StoriesAdapter(
    baseActivity: BaseActivity
) :
    RecyclerView.Adapter<StoriesAdapter.MyViewHolder>() {
    var baseActivity: BaseActivity? = null

    init {
        this.baseActivity = baseActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_stories,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV = itemView.titleTV
        val mainLL = itemView.mainLL
    }

    interface DrawerCallBack {
        fun drawerCallBack(postion: Int)
    }

}
