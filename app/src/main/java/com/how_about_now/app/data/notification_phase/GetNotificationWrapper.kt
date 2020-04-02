package com.how_about_now.app.data.notification_phase

data class GetNotificationWrapper(
    val code: String,
    val msg: ArrayList<GetNotificationMsg>
)