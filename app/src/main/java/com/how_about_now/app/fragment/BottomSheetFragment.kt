package com.how_about_now.app.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.how_about_now.app.R
import com.how_about_now.app.activity.ImageCropperActivity
import com.how_about_now.app.utils.AppConstants
import com.how_about_now.app.utils.GalleryImageCallBack
import kotlinx.android.synthetic.main.dialog_select_option.*


class BottomSheetFragment : BaseDialogFragment(), View.OnClickListener,
    GalleryImageCallBack.GalleryImageListener {


    private var outputfileUri: Uri? = null

    override fun onClick(v: View?) {
        when (v) {
            cameraTV -> {
                var cameraDialog = CameraDialog()
                cameraDialog.show(baseActivity!!.supportFragmentManager, "cameraTag")
                dismiss()
            }
            galleryTV -> {
                requestGalleryPermission()
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_select_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        galleryTV.setOnClickListener(this)
        cameraTV.setOnClickListener(this)
        selectLL.setOnClickListener(this)
        GalleryImageCallBack.getInstance(baseActivity!!).setGalleryImageListener(this)
    }

    fun requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(
                baseActivity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                baseActivity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                baseActivity!!, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), AppConstants.PERMISSIONS_REQUEST_ACCESS_GALLERY
            )
        } else {
            startGalleryIntent()
        }
    }

    fun startGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(galleryIntent, "Select Picture"),
            AppConstants.SELECT_FILE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.SELECT_FILE) {
                if (data != null) {
                    outputfileUri = data.data
                    var intent = Intent(baseActivity, ImageCropperActivity::class.java)
                    intent.putExtra("imageUri", outputfileUri)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            AppConstants.PERMISSIONS_REQUEST_ACCESS_GALLERY -> if (ContextCompat.checkSelfPermission(
                    baseActivity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    baseActivity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startGalleryIntent()
            }
        }
    }

    override fun onGalleryImageCallBack(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    outputfileUri = data.data
                    var intent = Intent(baseActivity, ImageCropperActivity::class.java)
                    intent.putExtra("imageUri", outputfileUri)
                    baseActivity!!.startActivity(intent)
                }
        }
    }
}