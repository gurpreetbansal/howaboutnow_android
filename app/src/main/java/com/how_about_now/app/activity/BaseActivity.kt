package com.how_about_now.app.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.how_about_now.app.R
import com.how_about_now.app.data.login_phase.Msg
import com.how_about_now.app.utils.*

/**
 *A simple [Activity] created by Anuj Kamboj.
 *
 */

open class BaseActivity : AppCompatActivity() {
    var exit = false
    lateinit var store: PrefStore
    private var progressDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtils.setDialog(this)
        store = PrefStore(this)

    }

    open fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

    }

    @SuppressLint("HardwareIds")
    fun getDeviceUniqueID(): String {
        return Settings.Secure.getString(
            getContentResolver(),
            Settings.Secure.ANDROID_ID
        )
    }

    fun initFCM() {
        val refreshToken = getDeviceUniqueID()
        if (!refreshToken!!.isEmpty()) {
            checkApi(refreshToken)
        }
    }

    private fun checkApi(refreshToken: String) {
        gotoLoginSignupActivity()
    }

    fun hideSoftKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun gotoFragment(fragment: Fragment, container: Int) {
        supportFragmentManager.beginTransaction()
            .replace(container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun callFragment(fragment: Fragment, container: Int) {
        supportFragmentManager.beginTransaction()
            .replace(container, fragment)
            .commit()
    }

    fun gotoFragment(fragment: Fragment, container: Int, bundle: Bundle) {
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun gotoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun gotoLoginSignupActivity() {
        val intent = Intent(this, LoginSignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("NewApi")
    fun backAction() {
        if (exit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "Press one more time to exit", Toast.LENGTH_SHORT).show()
            exit = true
            Handler().postDelayed({ exit = false }, (2 * 1000).toLong())
        }
    }

    interface PermissionCallback {
        fun permGranted()

        fun permDenied()
    }

    fun showSnackBar(message: String) {
        SnackBarManager.showSnackBar(message, this)
    }

    fun log(message: String) {
        CommonUtils.log(this, message)
    }

    fun showLoading() {
        showProgressDialog(this)
//        CommonUtils.showProgressDialog(this)
    }

    fun hideLoading() {
        hideProgressDialog()
//        CommonUtils.hideProgressDialog()
    }

    fun showMessage(message: String) {
        CommonUtils.showToast(this, message)
    }

    fun showMessage(@StringRes resId: Int) {
        showMessage(getString(resId))
    }

    fun isNetworkConnected(): Boolean {
        return NetworkUtils.isNetworkConnected(applicationContext)
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches())
    }

    fun isEmptyCheck(editText: EditText?): Boolean {
        return editText?.text?.toString()?.trim { it <= ' ' }?.isEmpty() ?: true
    }

    fun getTextValue(editText: EditText?): String {
        return editText?.text?.toString()?.trim { it <= ' ' } ?: ""
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionCallBack.getInstance(this)
            .onPermissionListener(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        GalleryImageCallBack.getInstance(this)
            .onGalleryImageListener(requestCode, resultCode, data)
    }

    fun saveProfileData(authData: Msg): Msg {
        store.save("authData", authData)
        return authData
    }

    fun getProfileData(): Msg {
        return store.getObject("authData", Msg::class.java)!!
    }

    open fun showProgressDialog(context: Context?) {
        hideProgressDialog()
        if (progressDialog != null) {
            progressDialog?.show()
            return
        }
        progressDialog = Dialog(context!!, R.style.transparent_dialog_borderless)
        val view = LayoutInflater.from(context).inflate(
            R.layout.progress_dialog,
            null,
            false
        )

        //        View view = View.inflate(context, R.layout.progress_dialog, null);
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(view)
        progressDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // txtMsgTV = (TextView) view.findViewById(R.id.txtMsgTV);
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    open fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing()) {
            try {
                progressDialog?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
