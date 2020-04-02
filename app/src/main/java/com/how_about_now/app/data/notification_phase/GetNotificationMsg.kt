package com.how_about_now.app.data.notification_phase

data class GetNotificationMsg(
    val friend_bday_noti: String,
    val like_noti: String,
    val match_noti: String,
    val new_message_noti: String,
    val offer_promotions_noti: String,
    val profile_visit_noti: String,
    val purchase_noti: String,
    val recommend_noti: String,
    val user_id: String
)