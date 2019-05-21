package com.baobomb.nlautouitest

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers

abstract class BaseView {
    fun assertIsDisplayed(id: Int) {
        Espresso.onView(ViewMatchers.withId(id)).inRoot(RootMatchers.isSystemAlertWindow()).noActivity().check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertIsNotDisplayed(id: Int) {
        Espresso.onView(ViewMatchers.withId(id)).inRoot(RootMatchers.isSystemAlertWindow()).noActivity().check(ViewAssertions.doesNotExist())
    }
}