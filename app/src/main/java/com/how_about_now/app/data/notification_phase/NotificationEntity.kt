package com.how_about_now.app.data.notification_phase

data class NotificationEntity(
    val friend_bday_noti: Int,
    val like_noti: Int,
    val match_noti: Int,
    val new_message_noti: Int,
    val offer_promotions_noti: Int,
    val profile_visit_noti: Int,
    val purchase_noti: Int,
    val recommend_noti: Int,
    val user_id: Int
)