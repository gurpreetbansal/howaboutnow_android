package com.how_about_now.app.application

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class MyApplication : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }
}