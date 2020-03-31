package com.how_about_now.app.data

data class FeedbackWrapper(
    val code: String,
    val msg: List<FeedbackMsg>
)