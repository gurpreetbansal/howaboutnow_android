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

import com.how_about_now.app.BuildConfig

/**
 * Created by Anuj on 27/5/19.
 */

object AppConstants {
    val BASE_URL = "http://68.183.74.38:8008/cashcool/api/"
    val DISPLAY_MESSAGE_ACTION = BuildConfig.APPLICATION_ID + ".DISPLAY_MESSAGE"
    val PLAY_SERVICES_RESOLUTION_REQUEST = 1234
    const val PERMISSIONS_REQUEST_ACCESS_CAMERA = 200
    const val PERMISSIONS_REQUEST_ACCESS_GALLERY = 201
    const val REQUEST_CAMERA = 123
    const val SELECT_FILE = 456
    val STATUS_CODE_SUCCESS = "success"
    val STATUS_CODE_FAILED = "failed"

    val API_STATUS_CODE_LOCAL_ERROR = 0

    val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"
    val AUTH_TOKEN = "auth_token"

    val REQUEST_CODE = 99
    val STATUS_OK = 200


}

