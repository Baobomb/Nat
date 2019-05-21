package com.baobomb.nlautouitest

import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObject
import android.support.test.uiautomator.UiSelector

open class Views {

    companion object {
        var uiDevice: UiDevice? = null
    }

    @ReflectionHelper.TestView("返回鍵", "back button")
    class NATIVE_BACK_BUTTON {
        fun press() {
            uiDevice?.pressBack()
        }
    }

    @ReflectionHelper.TestView("Home鍵", "home button")
    class NATIVE_HOME_BUTTON {
        fun press() {
            uiDevice?.pressHome()
        }
    }

    @ReflectionHelper.TestView("關閉螢幕", "sleep button")
    class NATIVE_SLEEP_BUTTON {
        fun press() {
            uiDevice?.sleep()
        }
    }

    @ReflectionHelper.TestView("喚醒螢幕", "wake button")
    class NATIVE_WAKEUP_BUTTON {
        fun press() {
            uiDevice?.wakeUp()
        }
    }

    @ReflectionHelper.TestView("最近開啟的App", "recent app")
    class NATIVE_RECENT_APP {
        fun press() {
            uiDevice?.pressRecentApps()
        }
    }

    @ReflectionHelper.TestView("最近開啟的App中的Whoscall", "recent app whoscall")
    class NATIVE_RECENT_APP_WHOSCALL {
        fun press() {
            val whoscallAppObject = UiObject(UiSelector().resourceId("gogolook.callgogolook2"))
            whoscallAppObject.click()
        }
    }
}