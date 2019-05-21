package com.baobomb.nlautouitest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.test.rule.ActivityTestRule

/**
 * Created by chenweilun on 2018/3/22.
 */

/**
 * Created by chenweilun on 2018/3/21.
 */

class ControllableActivityTestRule<T : Activity> : ActivityTestRule<T> {

    private lateinit var mSelfJavaClass: Class<T>

    constructor(activityClass: Class<T>) : super(activityClass, false) {}

    constructor(activityClass: Class<T>, initialTouchMode: Boolean) : super(activityClass, initialTouchMode, true) {}

    constructor(activityClass: Class<T>, initialTouchMode: Boolean, launchActivity: Boolean) : super(activityClass, initialTouchMode, launchActivity) {
        mSelfJavaClass = activityClass
    }

    fun finish() {
        finishActivity()
    }

    fun launchSelf(context: Context) {
        launchActivity(Intent(context, mSelfJavaClass))
    }
}
