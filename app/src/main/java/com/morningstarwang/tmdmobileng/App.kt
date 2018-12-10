package com.morningstarwang.tmdmobileng

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport

open class App : Application() {
    companion object {

        /**
         * -1 代表 无
         * 0 代表 油门
         * 1 代表 刹车
         */
        var peakFlag = -1

        var isCollecting = false
        var isPredicting = false
        var confusionValues = arrayListOf(
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray(), //ICT_NEW
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray(), //ICT_OLD
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray(), //ICT_HTC
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray(), //HWA
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray() //HWB
        )
        var currentConfusionValues = arrayListOf(
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray(), //ICT_NEW
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray(), //ICT_OLD
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray(), //ICT_HTC
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray(), //HWA
            Array<Array<Int?>>(9) { arrayOfNulls(9) }.toList().toTypedArray() //HWB
        )

        var predictResult = arrayListOf("N/A", "N/A", "N/A", "N/A", "N/A")
        var voteResult = arrayListOf("N/A", "N/A", "N/A", "N/A", "N/A")

        var totalCorrectCountVote = arrayListOf(0, 0, 0, 0, 0)
        var totalAllCountVote = arrayListOf(0, 0, 0, 0, 0)

        var currentCorrectCountVote = arrayListOf(0, 0, 0, 0, 0)
        var currentAllCountVote = arrayListOf(0, 0, 0, 0, 0)
    }

    init {
        for (i in 0..4) {
            for (j in 0..8) {
                for (k in 0..8) {
                    confusionValues[i][j][k] = 0
                }
            }
        }

        for (i in 0..4) {
            for (j in 0..8) {
                for (k in 0..8) {
                    currentConfusionValues[i][j][k] = 0
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        CrashReport.initCrashReport(applicationContext, "a46118cfab", true)
    }
}