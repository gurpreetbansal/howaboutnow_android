package com.how_about_now.app.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.how_about_now.app.R
import com.how_about_now.app.fragment.login_phase.LoginFragment
import com.how_about_now.app.fragment.login_phase.SignUpFragment
import kotlinx.android.synthetic.main.activity_login_sign_up.*

class LoginSignUpActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //            window.setStatusBarColor(0x00000000); // transparent
            window.statusBarColor =
                ContextCompat.getColor(this, R.color.white_color) // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }
        setContentView(R.layout.activity_login_sign_up)
        init()
    }

    private fun init() {
        signInBT.setOnClickListener(this)
        signUpBT.setOnClickListener(this)
        callFragment(SignUpFragment(), R.id.login_container)
    }

    override fun onClick(v: View?) {
        when (v) {
            signUpBT -> {
                signUpBT.setTextColor(ContextCompat.getColor(this, R.color.black_color))
                signInBT.setTextColor(ContextCompat.getColor(this, R.color.gray_color))
                signUpBT.background = ContextCompat.getDrawable(this, R.drawable.text_selector)
                signInBT.setBackgroundColor(ContextCompat.getColor(this, R.color.white_color))
                callFragment(SignUpFragment(), R.id.login_container)
            }
            signInBT -> {
                signInBT.setTextColor(ContextCompat.getColor(this, R.color.black_color))
                signUpBT.setTextColor(ContextCompat.getColor(this, R.color.gray_color))
                signInBT.background = ContextCompat.getDrawable(this, R.drawable.text_selector)
                signUpBT.setBackgroundColor(ContextCompat.getColor(this, R.color.white_color))
                gotoFragment(LoginFragment(), R.id.login_container)
            }
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.login_container)
        if (fragment is SignUpFragment) {
            backAction()
        } else {
            signUpBT.setTextColor(ContextCompat.getColor(this, R.color.black_color))
            signInBT.setTextColor(ContextCompat.getColor(this, R.color.gray_color))
            signUpBT.background = ContextCompat.getDrawable(this, R.drawable.text_selector)
            signInBT.setBackgroundColor(ContextCompat.getColor(this, R.color.white_color))
            supportFragmentManager.popBackStack()
        }
    }
}
