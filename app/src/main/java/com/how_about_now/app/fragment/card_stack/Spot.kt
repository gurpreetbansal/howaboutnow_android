package com.how_about_now.app.fragment.card_stack

data class Spot(
        val id: Long = counter++,
        val name: String,
        val city: String,
        val url: String
) {
    companion object {
        private var counter = 1L
    }
}