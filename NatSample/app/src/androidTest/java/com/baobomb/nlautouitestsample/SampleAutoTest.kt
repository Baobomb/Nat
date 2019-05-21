package com.baobomb.nlautouitestsample

import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.baobomb.nlautouitest.AssertMethods
import com.baobomb.nlautouitest.NLAutoUiTest
import com.baobomb.nlautouitest.ReflectionHelper
import com.baobomb.nlautouitest.RegularTests
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class SampleAutoTest : NLAutoUiTest() {
    companion object {
        const val PRESS_BACK_BUTTON = "Start a test unit : Open app" +
                "," +
                "Scroll main list view to position : 99" +
                "," +
                "Swipe main view pager left" +
                "," +
                "Wait until uiDevice show BtnRequestPermission" +
                "," +
                "Click BtnRequestPermission"
//        const val PRESS_BACK_BUTTON = "Start a test unit : Open app,Click BtnRequestPermission"
//        const val PRESS_BACK_BUTTON1 = "Start a test unit : Open app,Scroll main list view to position : 99,Swipe main view pager left,Click BtnRequestPermission,Press back button,OperationTestEnd"
//        const val PRESS_BACK_BUTTON2 = "Start a test unit : Open app,Scroll main list view to position : 99,Swipe main view pager left,Click BtnRequestPermission,Press back button,OperationTestEnd"
    }

    @Before
    fun beforeTest() {
        globalContext = MyApplication.globalContext
        RegularTests.TESTS = arrayOf(PRESS_BACK_BUTTON)
        ReflectionHelper.viewClasses = MoreViews::class.java.classes
        ReflectionHelper.operationMethodsClass = MoreMethods::class.java
        ReflectionHelper.assertionMethodsClass = AssertMethods::class.java
    }

    override fun test() {
        super.test()
    }
}