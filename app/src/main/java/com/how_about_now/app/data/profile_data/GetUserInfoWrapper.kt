package com.how_about_now.app.data.profile_data

data class GetUserInfoWrapper(
    val code: String,
    val msg: List<GetUserInfoMsg>
)