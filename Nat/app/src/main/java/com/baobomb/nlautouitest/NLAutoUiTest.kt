package com.baobomb.nlautouitest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.annotation.CallSuper
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.filters.LargeTest
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiWatcher
import android.util.Log
import org.junit.Rule
import org.junit.Test


@LargeTest
abstract class NLAutoUiTest {

    interface UITestOperationListener {
        fun onOperationStart()
        fun onOperationFinish()
    }

    interface UITestPrepareListener {
        fun onPrePareStart()
        fun onPrepareFinish()
    }

    companion object {
        val LOG_TAG = NLAutoUiTest::class.java.simpleName
        const val OPERATION_ACTION = "gogolook.callgogolook2.test.ACTION"
        const val SPECIAL_CASE_HANDLER_UIWATCHER_NAME = "special_case_handler_ui_watcher"
        const val METHODS_EXTRA_KEY = "Methods_bundle"

        const val PREPARES_ANNOUNCE_KEY_CHINESE = "建立測試環境 : "
        const val OPERATIONS_ANNOUNCE_KEY_CHINESE = "開始一個測試單元 : "
        const val ASSERT_ANNOUNCE_KEY_CHINESE = "預期 "

        const val SPECIAL_CASE_ANNOUNCE_KEY_CHINESE = "特殊情況 >> "
        const val SPECIAL_CASE_IF_KEY_CHINESE = "如果 "
        const val SPECIAL_CASE_STATUS_IS_KEY_CHINESE = " 是 "
        const val SPECIAL_CASE_THEN_KEY_CHINESE = " 就 "

        const val PREPARES_ANNOUNCE_KEY_ENGLISH = "Build test environment : "
        const val OPERATIONS_ANNOUNCE_KEY_ENGLISH = "Start a test unit : "

        const val ASSERT_ANNOUNCE_KEY_ENGLISH = "Assert "

        const val SPECIAL_CASE_ANNOUNCE_KEY_ENGLISH = "Special Case >> "
        const val SPECIAL_CASE_IF_KEY_ENGLISH = "If "
        const val SPECIAL_CASE_STATUS_IS_KEY_ENGLISH = " is "
        const val SPECIAL_CASE_THEN_KEY_ENGLISH = " then "

        const val VIEW_MATCHER_WITH_TEXT_PREFIX = "view with text : "

        const val SPLIT_REGEX = ","
        const val SPLIT_ASSERT_TIME_OUT_IN_OPERATION = " for "

        const val ASSERT_TIME_OUT_IN_OPERATION_MIN = " min"
        const val ASSERT_TIME_OUT_IN_OPERATION_SEC = " sec"
        const val ASSERT_TIME_OUT_IN_OPERATION_HOUR = " hour"

        var isOperating = false
        var isPreparing = false
        var isWaitingOperation = false

        val operationsAndAssertList = ArrayList<String>()
        var prepareOperationList = ArrayList<String>()
        val specialCaseList = ArrayList<SpecialCase>()

        var uiTestOperationListener: UITestOperationListener = object : UITestOperationListener {
            override fun onOperationStart() {
                isOperating = true
            }

            override fun onOperationFinish() {
                if (operationsAndAssertList.size > 0) {
                    operationsAndAssertList.removeAt(0)
                }
                isOperating = false
            }
        }

        var uiTestPrepareListener: UITestPrepareListener = object : UITestPrepareListener {
            override fun onPrePareStart() {
                isPreparing = true
            }

            override fun onPrepareFinish() {
                if (prepareOperationList.size > 0) {
                    prepareOperationList.removeAt(0)
                }
                isPreparing = false
            }
        }
        var specialCaseHandlerUiWatcher: UiWatcher = UiWatcher {
            while (true) {
                specialCaseList.forEach {
                    if (it.isHappend()) {
                        Log.d(LOG_TAG, "Special case happen")
                        it.handleIt()
                    }
                }
            }
            return@UiWatcher false
        }

        var globalContext: Context? = null
    }

    private lateinit var uiDevice: UiDevice

    var operationReceiver: BroadcastReceiver? = null
    val intentFilter = IntentFilter(OPERATION_ACTION)
    var isPrepareOperationHandling = false
    var isOperationAndAssertionHandling = false

    @Test
    @Throws(Exception::class)
    @CallSuper
    open fun test() {
        isWaitingOperation = true
//        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).registerWatcher(SPECIAL_CASE_HANDLER_UIWATCHER_NAME, specialCaseHandlerUiWatcher)
//        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).runWatchers()
        runRegularTest()
        registerTestsStoryFromBroadcast()
//        MyApplication.getGlobalContext().registerReceiver(operationReceiver, intentFilter)
        do {
            if (prepareOperationList.size > 0 && !isPreparing && !isPrepareOperationHandling) {
                isPreparing = true
                ReflectionHelper.invokePrepareMethodsAnnotatedWithOperationName(prepareOperationList[0])
            } else if (operationsAndAssertList.size > 0 && !isOperating && !isOperationAndAssertionHandling) {
                isOperating = true
                setUiDevice(UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()))
                handleOperationOrAssertion(operationsAndAssertList[0])
            }
        } while (isWaitingOperation || operationsAndAssertList.size > 0)

//        MyApplication.getGlobalContext().unregisterReceiver(operationReceiver)
//        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).removeWatcher(SPECIAL_CASE_HANDLER_UIWATCHER_NAME)
    }

    private fun handlePrepare(prepares: Array<String>) {
        for (i in 0..(prepares.size - 1)) {
            val operation = prepares[i]
            prepareOperationList.add(operation)
        }
        isPrepareOperationHandling = false
    }

    class SpecialCase(val component: String, val status: String, val handlerAction: String) {
        private fun (() -> Any?).assertResult(assertSuccessMessage: String?, assertFailMessage: String?): Boolean = try {
            invoke()
            Log.d(LOG_TAG, "Assert result : $assertSuccessMessage")
            true
        } catch (ex: Exception) {
            if (assertFailMessage.isNullOrEmpty()) {
                Log.d(LOG_TAG, ex.printStackTrace().toString())
                false
            } else {
                Log.d(LOG_TAG, assertFailMessage)
                false
            }
        }


        fun isHappend(): Boolean {
            if (status.contains("displayed")) {
//                val view = OperationViews.assertableView[component]
//                if (view != null) {
//                    return { view.assertIsDisplayed() }.assertResult("Status not happen", "Status happen")
//                }
            }
            return false
        }

        fun handleIt() {
            if (handlerAction.contains("Click")) {
                val nameOrDesc = handlerAction.substringAfter("Click ")
                try {
                    if (nameOrDesc.contains(VIEW_MATCHER_WITH_TEXT_PREFIX)) {
                        Espresso.onView(ViewMatchers.withText(nameOrDesc.substringAfter(VIEW_MATCHER_WITH_TEXT_PREFIX))).perform(click())
                    } else {
//                        OperationViews.clickableViews[nameOrDesc]?.click()
                    }
                } catch (e: Exception) {
                    Log.d(LOG_TAG, e.message)
                }
            }
        }
    }

    private fun handleSpecialHandler(specialCases: Array<String>) {
        specialCases.forEach {
            var component: String? = null
            var status: String? = null
            var handlerAction: String? = null

            if (it.startsWith(SPECIAL_CASE_IF_KEY_CHINESE)) {
                component = it.substringAfter(SPECIAL_CASE_IF_KEY_CHINESE).substringBefore(SPECIAL_CASE_STATUS_IS_KEY_CHINESE)
                status = it.substringBefore(SPECIAL_CASE_STATUS_IS_KEY_CHINESE).substringBefore(SPECIAL_CASE_THEN_KEY_CHINESE)
                handlerAction = it.substringAfter(SPECIAL_CASE_THEN_KEY_CHINESE)
            } else if (it.startsWith(SPECIAL_CASE_IF_KEY_ENGLISH)) {
                component = it.substringAfter(SPECIAL_CASE_IF_KEY_ENGLISH).substringBefore(SPECIAL_CASE_STATUS_IS_KEY_ENGLISH)
                status = it.substringAfter(SPECIAL_CASE_STATUS_IS_KEY_ENGLISH).substringBefore(SPECIAL_CASE_THEN_KEY_ENGLISH)
                handlerAction = it.substringAfter(SPECIAL_CASE_THEN_KEY_ENGLISH)
            }

            if (component != null && status != null && handlerAction != null) {
                specialCaseList.add(SpecialCase(component, status, handlerAction))
            }
        }
    }

    private fun injectOperationAndAssertions(operations: Array<String>) {
        for (i in 0..(operations.size - 1)) {
            val operation = operations[i]
            operationsAndAssertList.add(operation)
        }
        isOperationAndAssertionHandling = false
    }

    private fun setUiDevice(uiDevice: UiDevice) {
        this.uiDevice = uiDevice
        Views.uiDevice = uiDevice
    }

    private fun handleOperationOrAssertion(operationOrAssertion: String) {
        if (operationOrAssertion.startsWith(ASSERT_ANNOUNCE_KEY_ENGLISH)) {
            ReflectionHelper.invokeAssertMethodsAnnotatedWithOperationName(operationOrAssertion.substringAfter(ASSERT_ANNOUNCE_KEY_ENGLISH))
        } else if (operationOrAssertion.startsWith(ASSERT_ANNOUNCE_KEY_CHINESE)) {
            ReflectionHelper.invokeAssertMethodsAnnotatedWithOperationName(operationOrAssertion.substringAfter(ASSERT_ANNOUNCE_KEY_CHINESE))
        } else {
            ReflectionHelper.invokeOperationAnnotatedWithOperationName(operationOrAssertion)
        }
    }

    private fun runRegularTest() {
        RegularTests.TESTS?.forEach {
            handleTestStory(it)
        }
    }


    private fun registerTestsStoryFromBroadcast() {
        operationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val story = intent?.getStringExtra(METHODS_EXTRA_KEY)
                story?.run {
                    handleTestStory(story)
                }
            }
        }
    }

    private fun handleTestStory(story: String) {
        val chineseOperationsAndAssertions = story.substringAfter(OPERATIONS_ANNOUNCE_KEY_CHINESE, "")
        val engOperationsAndAssertions = story.substringAfter(OPERATIONS_ANNOUNCE_KEY_ENGLISH, "")

        val engSpecialCases = story.substringAfter(SPECIAL_CASE_ANNOUNCE_KEY_ENGLISH, "")
        val chineseSpecialCases = story.substringAfter(SPECIAL_CASE_ANNOUNCE_KEY_CHINESE, "")

        val chinesePrepares = story.substringAfter(PREPARES_ANNOUNCE_KEY_CHINESE, "")
        val engPrepares = story.substringAfter(PREPARES_ANNOUNCE_KEY_ENGLISH, "")


        isPrepareOperationHandling = true
        isOperationAndAssertionHandling = true
        if (chinesePrepares.isNotEmpty()) {
            handlePrepare(chinesePrepares.split(SPLIT_REGEX).toTypedArray())
        }
        if (engPrepares.isNotEmpty()) {
            handlePrepare(engPrepares.split(SPLIT_REGEX).toTypedArray())
        }

        if (engSpecialCases.isNotEmpty()) {
            handleSpecialHandler(engSpecialCases.split(SPLIT_REGEX).toTypedArray())
        }
        if (chineseSpecialCases.isNotEmpty()) {
            handleSpecialHandler(chineseSpecialCases.split(SPLIT_REGEX).toTypedArray())
        }
        if (chineseOperationsAndAssertions.isNotEmpty()) {
            injectOperationAndAssertions(chineseOperationsAndAssertions.split(SPLIT_REGEX).toTypedArray())
        }
        if (engOperationsAndAssertions.isNotEmpty()) {
            injectOperationAndAssertions(engOperationsAndAssertions.split(SPLIT_REGEX).toTypedArray())
        }
    }
}