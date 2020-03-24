package com.how_about_now.app.data.login_phase

data class Msg(
    val action: String,
    val device: String,
    val device_token: String,
    val first_name: String,
    val gender: String,
    val last_name: String,
    val lat: String,
    val lng: String,
    val email: String,
    val user_id: Int
)