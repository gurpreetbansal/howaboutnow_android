package com.how_about_now.app.data.near_by_me

data class NearByMeMsg(
    val about_me: String,
    val job_title: String,
    val gender: String,
    val birthday: String,
    val age: String,
    val first_name: String,
    val last_name: String,
    val image1: String,
    val image2: String,
    val image3: String,
    val image4: String,
    val image5: String,
    val image6: String,
    val total_like: Int,
    val total_super_like: Int,
    val latitude: String,
    val longitude: String
)