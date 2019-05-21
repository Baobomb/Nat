package com.baobomb.nlautouitestsample

import android.util.Log
import com.baobomb.nlautouitest.ControllableActivityTestRule
import com.baobomb.nlautouitest.NLAutoUiTest
import com.baobomb.nlautouitest.OperationMethods
import com.baobomb.nlautouitest.ReflectionHelper
import org.junit.Rule

class MoreMethods : OperationMethods() {


    @get:Rule
    val mainRule = ControllableActivityTestRule(MainActivity::class.java, true, false)

    @ReflectionHelper.TestOperateMethod("關閉App", "Close app")
    fun closeApp() {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        if (NLAutoUiTest.globalContext != null) {
            mainRule.finish()
        } else {
            Log.d(NLAutoUiTest.LOG_TAG, "Global context null")
        }
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }

    @ReflectionHelper.TestOperateMethod("打開App", "Open app")
    fun openApp() {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        if (NLAutoUiTest.globalContext != null) {
            mainRule.launchSelf(NLAutoUiTest.globalContext!!)
        } else {
            Log.d(NLAutoUiTest.LOG_TAG, "Global context null")
        }
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }

}