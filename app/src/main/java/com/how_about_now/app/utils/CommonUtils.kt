/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.how_about_now.app.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.how_about_now.app.BuildConfig
import com.how_about_now.app.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.io.IOException
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**  * Created by Anuj on 27/5/19.   */

object CommonUtils {

    private val TAG = "CommonUtils"
    private var progressDialog: Dialog? = null
    private var toast: Toast? = null

    val timeStamp: String
        get() = SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(Date())

    fun showProgressDialog(context: Context) {
        hideProgressDialog()
        if (progressDialog != null) {
            progressDialog!!.show()
            return
        }
    }

    fun setDialog(context: Context) {
        progressDialog = Dialog(context, R.style.transparent_dialog_borderless)
        val view = View.inflate(context, R.layout.progress_dialog, null)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(view)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // txtMsgTV = (TextView) view.findViewById(R.id.txtMsgTV);
        progressDialog!!.setCancelable(false)
    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun showToast(context: Context, msg: String) {
        if (toast == null)
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        toast!!.setText(msg)
        toast!!.show()
    }

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun checkPlayServices(context: Context): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(
                    context as Activity,
                    resultCode,
                    AppConstants.PLAY_SERVICES_RESOLUTION_REQUEST
                )
                    .show()
            } else {
                (context as Activity).finish()
            }
            return false
        }
        return true
    }

    @SuppressLint("HardwareIds", "NewApi")
    fun getUniqueDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @SuppressLint("NewApi")
    fun keyHash(context: Context): String {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val keyHash: String
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:>>>>>>>>>>>>>>>", "" + Base64.encodeToString(md.digest(), Base64.DEFAULT))
                return Base64.encodeToString(md.digest(), Base64.DEFAULT)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

    fun log(context: Context, message: String) {
        if (BuildConfig.DEBUG)
            Log.e(context.javaClass.simpleName, message)
    }

    fun isEmailValid(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String): String {

        val manager = context.assets
        val `is` = manager.open(jsonFileName)

        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        return String(buffer, Charset.forName("UTF-8"))
    }
}
