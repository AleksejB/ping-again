package com.thestart.pingagain.util

import com.qiniu.android.netdiag.Ping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Pinger {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val testLogger = TestLogger()

    fun pingHostShort(hostAddress: String, lam: (pingValue: Ping.Result?) -> Unit) {
        val callback = Callback(lam)
        scope.launch {
            Ping.start(hostAddress,PingCounts.shortPingsCount.toInt(), testLogger, callback)
        }
    }

    fun pingHostLong(hostAddress: String, lam: (pingValue: Ping.Result?) -> Unit) {
        val callback = Callback(lam)
        scope.launch {
            Ping.start(hostAddress, PingCounts.longPingsCount.toInt(), testLogger, callback)
        }
    }
}