package com.how_about_now.app.data.near_by_me

data class NearByMeEntity(
    val age_max: Int,
    val age_min: Int,
    val current_lat: String,
    val current_long: String,
    val distance: String,
    val gender: String,
    val user_id: String
)