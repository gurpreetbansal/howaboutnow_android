package com.how_about_now.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.how_about_now.app.R
import com.how_about_now.app.databinding.ActivitySplashBinding
import com.how_about_now.app.utils.AppConstants

class SplashActivity : BaseActivity() {
    private lateinit var activitySplashBinding: ActivitySplashBinding
    private val SPLASH_TIME_OUT = 3000
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var activitySplashBinding =
            DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        init()
        activitySplashBinding.root
    }

    private fun init() {
        Handler().postDelayed({
            if (store != null && store.getString(AppConstants.AUTH_TOKEN) != null && !store.getString(
                    AppConstants.AUTH_TOKEN
                )!!.isEmpty()
            ) {
                startActivity(Intent(this, MainActivity::class.java))
            } else startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT.toLong())

    }
}
