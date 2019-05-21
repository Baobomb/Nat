package com.baobomb.nlautouitest

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.v7.widget.RecyclerView
import org.hamcrest.Matchers

class OperationView : Views() {

    companion object {
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
    }

    @ReflectionHelper.TestView("測試列表", "main list view")
    class MainTitle : BaseView() {
        fun scroll(target: String) {
            scrollViewToTarget(R.id.rv_sample, target)
        }
    }
}