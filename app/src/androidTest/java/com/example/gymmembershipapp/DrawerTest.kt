package com.example.gymmembershipapp


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DrawerTest {

    @Test
    fun testOpenDrawer() {
        onView(withContentDescription("Open menu"))
            .perform(click())

        onView(withContentDescription("Drawer"))
            .check(matches(isDisplayed()))
    }
}
