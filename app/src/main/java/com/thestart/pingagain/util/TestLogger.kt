package com.thestart.pingagain.util

import com.qiniu.android.netdiag.Output

class TestLogger: Output {

    override fun write(line: String?) {
        //NO-OP
    }
}