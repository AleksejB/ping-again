package com.thestart.pingagain.util

import com.qiniu.android.netdiag.Ping

class Callback(val lam: (pingValue: Ping.Result?) -> Unit): Ping.Callback {

    override fun complete(r: Ping.Result?) {
        lam(r)
    }

}