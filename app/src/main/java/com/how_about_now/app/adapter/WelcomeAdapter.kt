package com.how_about_now.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity

class WelcomeAdapter(
    baseActivity: BaseActivity, imageArray: IntArray

) : PagerAdapter() {
    private var baseActivity: BaseActivity? = null
    private var imageArray: IntArray? = null

    init {
        this.baseActivity = baseActivity
        this.imageArray = imageArray
    }

    override fun getCount(): Int {
        return imageArray!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = LayoutInflater.from(container.getContext())
            .inflate(R.layout.adapter_welcome, container, false);
        container.addView(view, 0)

        var titleTV = view.findViewById<TextView>(R.id.titleTV)
        var bannerImage = view.findViewById<ImageView>(R.id.bannerImage)

        bannerImage.setImageResource(imageArray!![position])
        when (position) {
            0 -> {
                titleTV.setText(baseActivity!!.getString(R.string.descover_new_and_intersting))
            }
            1 -> {
                titleTV.setText(baseActivity!!.getString(R.string.swipe_right_to_like))
            }
            2 -> {
                titleTV.setText(baseActivity!!.getString(R.string.if_they_also_swipe))
            }
            3 -> {
                titleTV.setText(baseActivity!!.getString(R.string.only_people_you_have_matched_with_can_message_you))
            }
        }
        return view
    }
}