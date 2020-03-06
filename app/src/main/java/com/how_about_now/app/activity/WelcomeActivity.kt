package com.how_about_now.app.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.how_about_now.app.R
import com.how_about_now.app.adapter.WelcomeAdapter
import com.how_about_now.app.databinding.ActivityWelcomeBinding
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity(), View.OnClickListener {
    private val imageArray = intArrayOf(
        R.drawable.ic_normal,
        R.drawable.ic_like,
        R.drawable.ic_match,
        R.drawable.ic_chat
    )
    private lateinit var activityWelcomeBinding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWelcomeBinding =
            DataBindingUtil.setContentView<ActivityWelcomeBinding>(this, R.layout.activity_welcome)
        init()
        activityWelcomeBinding.root
    }

    private fun init() {
        val newAccount =
            "New here?" + " " + "<font color='#000000'>" + "SignUp new account" + "</font>"

        newAccountTV.setText(
            HtmlCompat.fromHtml(newAccount, Html.FROM_HTML_MODE_LEGACY),
            TextView.BufferType.SPANNABLE
        )

        viewPager.adapter = WelcomeAdapter(this, imageArray)
        newindicator.setViewPager(viewPager)

        newAccountTV.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            newAccountTV -> {
                startActivity(Intent(this, LoginSignUpActivity::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        backAction()
    }
}
