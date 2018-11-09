package com.morningstarwang.tmdmobileng

import android.app.Application

open class App: Application() {
    companion object {
        var isCollecting = false
    }
}