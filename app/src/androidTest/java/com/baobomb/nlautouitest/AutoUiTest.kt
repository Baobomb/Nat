package com.baobomb.nlautouitest

import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.baobomb.nlautouitest.sample.SampleApplication
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class AutoUiTest : NLAutoUiTest() {
    companion object {
        const val PRESS_BACK_BUTTON = "Start a test unit : Open app,Scroll main list view to position 10,Press back button,OperationTestEnd"
    }

    @Before
    fun beforeTest() {
        globalContext = SampleApplication.sGlobalContext
        RegularTests.TESTS = arrayOf(PRESS_BACK_BUTTON)
        ReflectionHelper.operationMethodsClass = OperationMethods::class.java
        ReflectionHelper.viewClasses = OperationView::class.java.classes
    }

    override fun test() {
        super.test()
    }
}