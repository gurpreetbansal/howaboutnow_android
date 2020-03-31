package com.how_about_now.app.data.profile_data

data class EditProfileEntity(
    val about_me: String,
    val birthday: String,
    val gender: String,
    val question_data: ArrayList<QuestionData>,
    val user_id: String
)