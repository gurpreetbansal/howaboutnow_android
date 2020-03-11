package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.adapter.StoriesAdapter
import com.how_about_now.app.adapter.UsersAdapter
import com.how_about_now.app.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_near_by.*

/**
 * A simple [Fragment] subclass.
 */
class NearByFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_near_by, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        storyRV.layoutManager = LinearLayoutManager(baseActivity!!, RecyclerView.HORIZONTAL, false)
        usersRV.layoutManager = GridLayoutManager(baseActivity!!, 2, RecyclerView.VERTICAL, false)

        usersRV.adapter = UsersAdapter(baseActivity!!)
        storyRV.adapter = StoriesAdapter(baseActivity!!)
    }

}
