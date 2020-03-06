package com.how_about_now.app.fragment.home_phase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        logoutTV.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            logoutTV -> {
                baseActivity!!.gotoLoginSignupActivity()
            }
        }
    }

}
