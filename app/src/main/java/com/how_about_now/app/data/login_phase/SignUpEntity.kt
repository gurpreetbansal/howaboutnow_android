package com.how_about_now.app.data.login_phase

data class SignUpEntity(
    val device: String,
    val device_token: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val lat: String,
    val lng: String,
    val password: String
)