package com.how_about_now.app.data.login_phase

data class LoginEntity(
    val device: String,
    val device_token: String,
    val email: String,
    val lat: String,
    val lng: String,
    val password: String
)