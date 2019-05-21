package com.baobomb.nlautouitest

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers

abstract class BaseView(val id: Int) {
    open fun scroll(target: String) {
        Views.scrollViewToTarget(id, target)
    }

    open fun swipe(direction: String) {
        if (direction == "left") {
            Views.swipeLeftOnView(id)
        } else {
            Views.swipeRightOnView(id)
        }
    }

    open fun assertIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(id)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    open fun assertIsNotDisplayed() {
        Espresso.onView(ViewMatchers.withId(id)).check(ViewAssertions.doesNotExist())
    }

    open fun click() {
        Espresso.onView(ViewMatchers.withId(id)).perform(ViewActions.click())
    }
}

abstract class BaseDialogChildView(id: Int) : BaseView(id) {
    override fun assertIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(id)).inRoot(RootMatchers.isSystemAlertWindow()).noActivity().check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    override fun assertIsNotDisplayed() {
        Espresso.onView(ViewMatchers.withId(id)).inRoot(RootMatchers.isSystemAlertWindow()).noActivity().check(ViewAssertions.doesNotExist())
    }
}