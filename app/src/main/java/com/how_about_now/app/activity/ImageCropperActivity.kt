package com.how_about_now.app.activity

import android.net.Uri
import android.os.Bundle
import com.how_about_now.app.R

class ImageCropperActivity : BaseActivity() {

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_cropper)
        getImageUriIntent()
    }

    private fun getImageUriIntent() {
        if (intent != null) {
            imageUri = intent.extras?.get("imageUri") as Uri
            showMessage(imageUri.toString())
        }
    }
}
