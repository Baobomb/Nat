package com.baobomb.nlautouitest.sample

import android.app.Application
import android.content.Context

class SampleApplication : Application() {
    companion object {
        var sGlobalContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        sGlobalContext = this
    }
}