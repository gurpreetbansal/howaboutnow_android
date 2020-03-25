package com.how_about_now.app.data.login_phase

data class ForgotPasswordWrapper(
    val code: String,
    val msg: List<ForgotPasswordMsg>
)