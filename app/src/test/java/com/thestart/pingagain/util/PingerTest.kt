package com.thestart.pingagain.util

import com.thestart.pingagain.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PingerTest {

    val hostAddress = "www.google.co.uk"
    val invalidHostAddress = "w.invalid.address"

    private lateinit var pinger: Pinger

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        pinger = Pinger()
    }

    @Test
    fun pingHost_success_returnHigherThanZero() {
        var result = 0L
        pinger.pingHost(hostAddress) {
            result = it
        }
        Thread.sleep(3000)//delay(3000)
        assertTrue(result > 0L)
    }
}