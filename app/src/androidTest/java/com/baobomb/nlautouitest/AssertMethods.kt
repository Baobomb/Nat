package com.baobomb.nlautouitest

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.util.Log

class AssertMethods {


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

    @ReflectionHelper.TestAssertMethod("正在顯示", "displayed")
    fun assertExist(assertTarget: String) {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        if (assertTarget.contains(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX)) {
            { Espresso.onView(ViewMatchers.withText(assertTarget.substringAfter(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX))).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }
                    .shouldNotThrow("Success, view displayed", "Assert view $assertTarget fail, it is not exist")
        } else {
            { ReflectionHelper.invokeViewsMethodWithOperation(assertTarget, "assertIsDisplayed") }
                    .shouldNotThrow("Success, view displayed", "Assert view $assertTarget fail, it is not exist")
        }
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }

    @ReflectionHelper.TestAssertMethod("沒有顯示", "not displayed")
    fun assertNotExist(assertTarget: String) {
        NLAutoUiTest.uiTestOperationListener.onOperationStart()
        if (assertTarget.contains(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX)) {
            { Espresso.onView(ViewMatchers.withText(assertTarget.substringAfter(NLAutoUiTest.VIEW_MATCHER_WITH_TEXT_PREFIX))).check(ViewAssertions.doesNotExist()) }
                    .shouldNotThrow("Success, view displayed", "Assert view $assertTarget fail, it is not exist")
        } else {
            { ReflectionHelper.invokeViewsMethodWithOperation(assertTarget, "assertIsDisplayed") }
                    .shouldNotThrow("Success, view displayed", "Assert view $assertTarget fail, it is not exist")
        }
        NLAutoUiTest.uiTestOperationListener.onOperationFinish()
    }
}