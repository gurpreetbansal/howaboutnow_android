package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.adapter.MessageAdapter
import com.how_about_now.app.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_message.*

/**
 * A simple [Fragment] subclass.
 */
class MessageFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        messageRV.layoutManager = LinearLayoutManager(baseActivity!!, RecyclerView.VERTICAL, false)
        messageRV.adapter = MessageAdapter(baseActivity!!)
    }

}
