package com.example.gymmembershipapp

import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.junit.Assert.*
import org.junit.runner.RunWith

class MainActivityTest {

    private lateinit var auth: FirebaseAuth

    @Before
    fun setup() {
        auth = mock(FirebaseAuth::class.java)
    }

    @Test
    fun testRedirectToLoginWhenUserIsNotAuthenticated() {
        `when`(auth.currentUser).thenReturn(null)

        val mainActivity = MainActivity()

        assertTrue(mainActivity.isFinishing)
    }
}
