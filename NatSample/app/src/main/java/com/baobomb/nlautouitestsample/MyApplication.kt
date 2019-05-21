package com.baobomb.nlautouitestsample

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        public var globalContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        globalContext = this
    }
}