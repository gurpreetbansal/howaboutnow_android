package com.how_about_now.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity
import kotlinx.android.synthetic.main.adapter_profile.view.*


class ProfileAdapter(
    baseActivity: BaseActivity,
    userImagesArrayList: ArrayList<String>
) :
    RecyclerView.Adapter<ProfileAdapter.MyViewHolder>() {
    var selectedPosition = 0
    var baseActivity: BaseActivity? = null
    var userImagesArrayList: ArrayList<String>? = null


    init {
        this.baseActivity = baseActivity
        this.userImagesArrayList = userImagesArrayList
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
        return userImagesArrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (userImagesArrayList!![position] != null && !userImagesArrayList!![position].isEmpty()) {
            Glide.with(baseActivity!!)
                .load(userImagesArrayList!![position])
                .into(holder.profileCIV)
        } else {
            holder.profileCIV.setImageResource(R.drawable.ic_default)
        }
        holder.addIV.setOnClickListener(View.OnClickListener {
        })
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileCIV = itemView.profileCIV
        val addIV = itemView.addIV
    }

    interface AddImageCallBack {
        fun onAddImageListener(position: Int)
    }

}
