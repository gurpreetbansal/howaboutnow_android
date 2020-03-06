package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.adapter.FavouriteAdapter
import com.how_about_now.app.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_favourites.*

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        favouriteRV.layoutManager =
            GridLayoutManager(baseActivity!!, 2, RecyclerView.VERTICAL, false)
        favouriteRV.adapter = FavouriteAdapter(baseActivity!!)

        matchTV.setOnClickListener(this)
        likedYouTV.setOnClickListener(this)
        youLikedTV.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            youLikedTV -> {
                matchTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.gray_color))
                likedYouTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.gray_color))
                youLikedTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.black_color))
                youLikedTV.background=ContextCompat.getDrawable(baseActivity!!,R.drawable.text_selector)
                matchTV.setBackgroundColor(ContextCompat.getColor(baseActivity!!,R.color.white_color))
                likedYouTV.setBackgroundColor(ContextCompat.getColor(baseActivity!!,R.color.white_color))
            }
            likedYouTV -> {
                matchTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.gray_color))
                youLikedTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.gray_color))
                likedYouTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.black_color))
                likedYouTV.background=ContextCompat.getDrawable(baseActivity!!,R.drawable.text_selector)
                matchTV.setBackgroundColor(ContextCompat.getColor(baseActivity!!,R.color.white_color))
                youLikedTV.setBackgroundColor(ContextCompat.getColor(baseActivity!!,R.color.white_color))
            }
            matchTV -> {
                likedYouTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.gray_color))
                youLikedTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.gray_color))
                matchTV.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.black_color))
                matchTV.background=ContextCompat.getDrawable(baseActivity!!,R.drawable.text_selector)
                youLikedTV.setBackgroundColor(ContextCompat.getColor(baseActivity!!,R.color.white_color))
                likedYouTV.setBackgroundColor(ContextCompat.getColor(baseActivity!!,R.color.white_color))
            }
        }
    }

}
