package com.how_about_now.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity
import com.how_about_now.app.data.DrawerData
import kotlinx.android.synthetic.main.adapter_drawer.view.*
import java.util.*


class DrawerAdapter(
    drawerCallBack: DrawerCallBack,
    drawerDataArrayList: ArrayList<DrawerData>,
    baseActivity: BaseActivity
) :
    RecyclerView.Adapter<DrawerAdapter.MyViewHolder>() {
    var selectedPosition = 0
    var baseActivity: BaseActivity? = null
    var drawerCallBack: DrawerCallBack? = null
    lateinit var drawerDataArrayList: ArrayList<DrawerData>

    init {
        this.baseActivity = baseActivity
        this.drawerCallBack = drawerCallBack
        this.drawerDataArrayList = drawerDataArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_drawer,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return drawerDataArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (selectedPosition == holder.adapterPosition)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(baseActivity!!, R.color.view_color));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(baseActivity!!, android.R.color.white));

        holder.titleTV.text = drawerDataArrayList.get(position).title
        holder.titleTV.setCompoundDrawablesWithIntrinsicBounds(drawerDataArrayList.get(position).icon, 0, 0, 0)
        holder.mainLL.setOnClickListener(View.OnClickListener {
            drawerCallBack?.drawerCallBack(holder.adapterPosition)
            selectedPosition = holder.adapterPosition;
            notifyDataSetChanged();
        })
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV = itemView.titleTV
        val mainLL = itemView.mainLL
    }

    interface DrawerCallBack {
        fun drawerCallBack(postion: Int)
    }

}
