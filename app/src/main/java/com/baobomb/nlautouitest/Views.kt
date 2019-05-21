package com.baobomb.nlautouitest

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObject
import android.support.test.uiautomator.UiSelector
import android.support.v7.widget.RecyclerView
import org.hamcrest.Matchers

open class Views {

    companion object {
        var uiDevice: UiDevice? = null

        fun scrollViewToTarget(id: Int, target: String) {
            if (target.startsWith("to position : ")) {
                val position = target.substringAfter("position : ").toInt()
                Espresso.onView(ViewMatchers.withId(id))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                                position,
                                ViewActions.scrollTo()))
            } else if (target.startsWith("to view : ")) {
                val viewText = target.substringAfter("to view with text : ")
                Espresso.onView(ViewMatchers.withId(id))
                        .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                                ViewMatchers.hasDescendant(Matchers.allOf(ViewMatchers.withText(viewText), ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))),
                                ViewActions.scrollTo()))
            }
        }

        fun swipeLeftOnView(id: Int) {
            Espresso.onView(ViewMatchers.withId(id)).perform(ViewActions.swipeLeft())
        }

        fun swipeRightOnView(id: Int) {
            Espresso.onView(ViewMatchers.withId(id)).perform(ViewActions.swipeRight())
        }

        fun clickView(id: Int) {
            Espresso.onView(ViewMatchers.withId(id)).perform(ViewActions.click())
        }
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