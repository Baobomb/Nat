package com.baobomb.nlautouitest

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.baobomb.nlautouitest.sample.SampleActivity
import org.hamcrest.Matchers
import org.junit.Rule


open class OperationMethods {


    @get:Rule
    val mainRule = ControllableActivityTestRule(SampleActivity::class.java, true, false)

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

    fun (() -> Any?).shouldNotThrow(assertSuccessMessage: String?, assertFailMessage: String?): Boolean = try {
        invoke()
        Log.d(NLAutoUiTest.LOG_TAG, "Assert result : $assertSuccessMessage")
        true
    } catch (ex: Exception) {
        if (assertFailMessage.isNullOrEmpty()) {
            Log.d(NLAutoUiTest.LOG_TAG, ex.printStackTrace().toString())
            false
        } else {
            Log.d(NLAutoUiTest.LOG_TAG, assertFailMessage)
            false
        }
    }

    @ReflectionHelper.TestOperateMethod("結束測試", "OperationTestEnd")
    fun endTest() {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        Log.d(NLAutoUiTest.LOG_TAG, "endTest")
        NLAutoUiTest.isWaitingOperation = false
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }

    @ReflectionHelper.TestOperateMethod("點擊", "Click")
    fun click(nameOrDesc: String) {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        try {
            Log.d(NLAutoUiTest.LOG_TAG, "click $nameOrDesc")
            if (nameOrDesc.contains(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX)) {
                Espresso.onView(ViewMatchers.withText(nameOrDesc.substringAfter(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX))).perform(ViewActions.click()).withFailureHandler { error, viewMatcher ->
                    NLAutoUiTest.uiTestOperationListener.onOperationFinish()
                }
            } else {
                ReflectionHelper.invokeViewsMethodWithOperation(nameOrDesc, "click")
            }
        } catch (e: Exception) {
            Log.d(NLAutoUiTest.LOG_TAG, e.message)
        }
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }

    @ReflectionHelper.TestOperateMethod("捲動", "Scroll")
    fun scrollRecyclerView(nameOrDescAndTargetScroll: String) {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        try {
            val nameOrDesc = nameOrDescAndTargetScroll.substringBefore(" to")
            val targetScroll = nameOrDescAndTargetScroll.substringAfter("$nameOrDesc ")
            if (nameOrDesc.contains(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX)) {
                Espresso.onView(ViewMatchers.withText(nameOrDesc.substringAfter(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX)))
                        .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                                ViewMatchers.hasDescendant(Matchers.allOf(ViewMatchers.withText(targetScroll), ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))),
                                ViewActions.scrollTo()))
                        .withFailureHandler { error, viewMatcher ->
                            NLAutoUiTest.uiTestOperationListener.onOperationFinish()
                        }
            } else {
                ReflectionHelper.invokeViewsMethodWithOperation(nameOrDesc, "scroll", targetScroll)
            }
        } catch (e: Exception) {

        }
    }


    @ReflectionHelper.TestOperateMethod("按下", "Press")
    fun press(nameOrDesc: String) {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        try {
            Log.d(NLAutoUiTest.LOG_TAG, "click $nameOrDesc")
            ReflectionHelper.invokeViewsMethodWithOperation(nameOrDesc, "press")
//            OperationViews.nativeButtons[nameOrDesc]?.press()
        } catch (e: Exception) {
            Log.d(NLAutoUiTest.LOG_TAG, e.message)
        }
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }

    @ReflectionHelper.TestOperateMethod("等待", "Force wait")
    fun forceWait(waitTime: String) {
        var timeOut: Long = 5000
        when {
            waitTime.contains(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_SEC) -> {
                timeOut = waitTime.substringBefore(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_SEC).toLong() * 1000
            }
            waitTime.contains(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_MIN) -> {
                timeOut = waitTime.substringBefore(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_SEC).toLong() * 1000 * 60
            }
            waitTime.contains(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_HOUR) -> {
                timeOut = waitTime.substringBefore(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_SEC).toLong() * 1000 * 60 * 60
            }
        }
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        val startTime = System.currentTimeMillis()
        do {
            Log.d("waitOnView", "waiting")
        } while (System.currentTimeMillis() - startTime <= timeOut)
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }

    @ReflectionHelper.TestOperateMethod("等待直到", "Wait until uiDevice show")
    fun waitOnView(viewNameOrDesc: String) {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        val startTime = System.currentTimeMillis()
        var isViewShown = false

        val target = viewNameOrDesc.substringBefore(NLAutoUiTest.SPLIT_ASSERT_TIME_OUT_IN_OPERATION)
        val timeOutDesc = viewNameOrDesc.substringAfter(NLAutoUiTest.SPLIT_ASSERT_TIME_OUT_IN_OPERATION, "5 sec")
        var timeOut: Long = 5000
        when {
            timeOutDesc.contains(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_SEC) -> {
                timeOut = timeOutDesc.substringBefore(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_SEC).toLong() * 1000
            }
            timeOutDesc.contains(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_MIN) -> {
                timeOut = timeOutDesc.substringBefore(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_SEC).toLong() * 1000 * 60
            }
            timeOutDesc.contains(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_HOUR) -> {
                timeOut = timeOutDesc.substringBefore(NLAutoUiTest.ASSERT_TIME_OUT_IN_OPERATION_SEC).toLong() * 1000 * 60 * 60
            }
        }
        do {
            isViewShown = if (target.contains(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX)) {
                { Espresso.onView(ViewMatchers.withText(target.substringAfter(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX))).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }
                        .shouldNotThrow("Success, view displayed", "Assert view $viewNameOrDesc fail, it is still not exist")
            } else {
                { ReflectionHelper.invokeViewsMethodWithOperation(target, "assertIsDisplayed") }
                        .shouldNotThrow("Success, view displayed", "Assert view $viewNameOrDesc fail, it is still not exist")
            }
            Log.d("waitOnView", "wait until $target shown for $timeOut")
        } while (!isViewShown && (System.currentTimeMillis() - startTime) < timeOut)
        if (isViewShown) {
            Log.d("waitOnView", "$target is shown, it take ${System.currentTimeMillis() - startTime}")
        } else {
            Log.d("waitOnView", "Time out, $target is not shown")
        }
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }
}