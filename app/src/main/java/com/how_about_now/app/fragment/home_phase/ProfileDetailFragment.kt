package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.adapter.*
import com.how_about_now.app.data.DrawerData
import com.how_about_now.app.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile_detail.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileDetailFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        var giftsArrayList = ArrayList<DrawerData>()
        val icon = arrayOf<Int>(
            R.drawable.ic_diamond,
            R.drawable.ic_egg,
            R.drawable.ic_king,
            R.drawable.ic_ring,
            R.drawable.ic_choco,
            R.drawable.ic_heart,
            R.drawable.ic_lolipop,
            R.drawable.ic_burger
        )
        for (i in icon.indices) {
            val data = DrawerData()
            data.icon = icon[i]
            data.title = ""
            giftsArrayList.add(data)
        }

        giftsRV.layoutManager = GridLayoutManager(baseActivity!!, 4, RecyclerView.VERTICAL, false)
        interestRV.layoutManager =
            GridLayoutManager(baseActivity!!, 4, RecyclerView.VERTICAL, false)
        moviesRV.layoutManager = GridLayoutManager(baseActivity!!, 4, RecyclerView.VERTICAL, false)
        musicRV.layoutManager = GridLayoutManager(baseActivity!!, 4, RecyclerView.VERTICAL, false)
        televisionRV.layoutManager =
            GridLayoutManager(baseActivity!!, 4, RecyclerView.VERTICAL, false)
        booksRV.layoutManager = GridLayoutManager(baseActivity!!, 4, RecyclerView.VERTICAL, false)

        giftsRV.adapter = GiftsAdapter(baseActivity!!, giftsArrayList)
        interestRV.adapter = InterestAdapter(baseActivity!!, giftsArrayList)
        moviesRV.adapter = MoviesAdapter(baseActivity!!, giftsArrayList)
        musicRV.adapter = MusicAdapter(baseActivity!!, giftsArrayList)
        booksRV.adapter = BooksAdapter(baseActivity!!, giftsArrayList)
        televisionRV.adapter = TelevisionAdapter(baseActivity!!, giftsArrayList)
    }

}
