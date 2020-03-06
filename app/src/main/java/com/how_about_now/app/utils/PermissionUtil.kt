package com.how_about_now.app.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Html
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity

object PermissionUtil {


    private var dialog: Dialog? = null

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        context: BaseActivity,
        permCallback: BaseActivity.PermissionCallback?
    ) {
        var permGrantedBool = false
        when (requestCode) {
            /*AppConstants.REQUEST_CODE*/ 99 -> {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    context.showSnackBar(
                        context.getString(R.string.not_sufficient_permissions)
                                + context.getString(R.string.app_name)
                                + context.getString(R.string.permissions)
                    )
                    permGrantedBool = false
                    break
                } else {
                    permGrantedBool = true
                }
            }
            if (permCallback != null) {
                if (permGrantedBool)
                    permCallback.permGranted()
                else {
                    showPermissionDialog(context)
                    permCallback.permDenied()
                }
            }
        }
        }
    }

    fun checkWriteSettingPermission(context: Activity, permCallback: BaseActivity.PermissionCallback) {
        val permission: Boolean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context)
        } else {
            permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_SETTINGS
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (permission) {
            permCallback.permGranted()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + context.packageName)
                context.startActivityForResult(intent, /*AppConstants.REQUEST_CODE*/99)
            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.WRITE_SETTINGS), /*AppConstants.REQUEST_CODE*/
                    99
                )
            }
        }
    }

    private fun showPermissionDialog(context: BaseActivity) {
        if (dialog != null && dialog!!.isShowing)
            return
        val alertDialogBuilder: AlertDialog.Builder
        alertDialogBuilder = AlertDialog.Builder(context, R.style.DialogStyle)
        alertDialogBuilder.setTitle("Permissions Required")
            .setMessage("You have forcefully denied some of the required permissions " + "for this action. Please open settings, go to permissions and allow them.")
            .setPositiveButton(Html.fromHtml("<font color='#000000'>Settings</font>")) { dialog, which ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            .setNegativeButton(Html.fromHtml("<font color='#000000'>Cancel</font>")) { dialog, which ->
                PermissionUtil.dialog = null
                showPermissionDialog(context)
            }
            .setCancelable(false)

        dialog = alertDialogBuilder.create()
        dialog!!.show()
    }

}
