package com.baobomb.nlautouitestsample

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.baobomb.nlautouitest.BaseView
import com.baobomb.nlautouitest.ReflectionHelper
import com.baobomb.nlautouitest.Views

class MoreViews : Views() {

    @ReflectionHelper.TestView("測試列表", "main list view")
    class MainScrollView : BaseView(R.id.rv_sample)

    @ReflectionHelper.TestView("主頁面ViewPager", "main view pager")
    class MainViewPager : BaseView(R.id.vp_sample)

    @ReflectionHelper.TestView("請求權限按鈕", "BtnRequestPermission")
    class RequestPermissionBtn : BaseView(R.id.bt_request_permission) {
        override fun click() {
            Espresso.onView(withId(id)).perform(ViewActions.click())
        }
    }
}