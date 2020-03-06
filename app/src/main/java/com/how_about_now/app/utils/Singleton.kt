package com.plendo.app.utils

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Singleton {

    private var instance: Singleton? = null
    var listner: AddButtonListener? = null
    var buttonListner: ButtonListener? = null
    var context: Context? = null


    @Synchronized
    fun createInstance(context: Context) {
        if (instance == null) {
            instance = Singleton(context)
        }
    }

    private fun Singleton(context: Context): Singleton? {
        this.context = context
        return this
    }

    fun getInstance(context: Context): Singleton? {
        if (instance == null) createInstance(context)
        return instance
    }

    open fun setAddButtonListener(listner: AddButtonListener) {
        this.listner = listner
    }

    open fun setButtonListener(listner: ButtonListener) {
        this.buttonListner = listner
    }

    fun onAddButtonListene() {
        if (listner != null) {
            listner!!.loginButtonCallBack()
        }
    }

    fun onButtonListene(boolean: Boolean) {
        if (buttonListner != null) {
            buttonListner!!.buttonCallBack(boolean)
        }
    }

    interface AddButtonListener {
        fun loginButtonCallBack()
    }

    interface ButtonListener {
        fun buttonCallBack(boolean: Boolean)
    }
}