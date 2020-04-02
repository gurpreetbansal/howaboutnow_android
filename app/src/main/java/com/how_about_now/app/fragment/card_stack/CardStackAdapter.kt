package com.how_about_now.app.fragment.card_stack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.bumptech.glide.Glide

class CardStackAdapter(
    private var spots: List<Spot> = emptyList(), private var buttonCallBack: ButtonsCallBack
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position]
        holder.name.text = "${spot.id}. ${spot.name}"
        holder.city.text = spot.city
        Glide.with(holder.image)
            .load(spot.url)
            .into(holder.image)
        holder.itemView.setOnClickListener { v ->
            Toast.makeText(v.context, spot.name, Toast.LENGTH_SHORT).show()
        }

        holder.skip_button.setOnClickListener {
            buttonCallBack.onSkipButton(spot.userId)
        }
        holder.rewind_button.setOnClickListener {
            buttonCallBack.onRewindButton(spot.userId)
        }
        holder.like_button.setOnClickListener {
            buttonCallBack.onLikeButton(spot.userId)
        }
    }

    override fun getItemCount(): Int {
        return spots.size
    }

    fun setSpots(spots: List<Spot>) {
        this.spots = spots
    }

    fun getSpots(): List<Spot> {
        return spots
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.item_name)
        var city: TextView = view.findViewById(R.id.item_city)
        var image: ImageView = view.findViewById(R.id.item_image)

        var skip_button: ImageView = view.findViewById(R.id.skip_button)
        var rewind_button: ImageView = view.findViewById(R.id.rewind_button)
        var like_button: ImageView = view.findViewById(R.id.like_button)
    }

    interface ButtonsCallBack {
        fun onSkipButton(effective_user_id:String)
        fun onRewindButton(effective_user_id:String)
        fun onLikeButton(effective_user_id:String)
    }

}