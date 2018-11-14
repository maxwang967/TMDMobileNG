package com.morningstarwang.tmdmobileng.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log.e
import com.morningstarwang.tmdmobileng.REAL_MODE
import com.morningstarwang.tmdmobileng.TIMESTAMP
import com.morningstarwang.tmdmobileng.bean.SensorData
import com.morningstarwang.tmdmobileng.utils.runOnIoThread
import java.io.File

/**
 * 处理Service广播发送的一个窗口的数据
 */
class SensorDataReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val bundle = intent?.getBundleExtra("window_data")
        runOnIoThread {
            e("receiver-Thread", Thread.currentThread().name)
            if (bundle != null) {
                val laccList: ArrayList<SensorData>? = bundle.getParcelableArrayList("laccList")
                val accList: ArrayList<SensorData>? = bundle.getParcelableArrayList("accList")
                val gyrList: ArrayList<SensorData>? = bundle.getParcelableArrayList("gyrList")
                val magList: ArrayList<SensorData>? = bundle.getParcelableArrayList("magList")
                val pressureList = bundle.getFloatArray("pressureList")
                for (i in 0 until 449) {
                    val laccX = laccList?.get(i)?.x
                    val laccY = laccList?.get(i)?.y
                    val laccZ = laccList?.get(i)?.z

                    val accX = accList?.get(i)?.x
                    val accY = accList?.get(i)?.y
                    val accZ = accList?.get(i)?.z

                    val gyrX = gyrList?.get(i)?.x
                    val gyrY = gyrList?.get(i)?.y
                    val gyrZ = gyrList?.get(i)?.z

                    val magX = magList?.get(i)?.x
                    val magY = magList?.get(i)?.y
                    val magZ = magList?.get(i)?.z

                    val pressure = pressureList?.get(i)

                    val content =
                        "$accX,$accY,$accZ,$laccX,$laccY,$laccZ,$gyrX,$gyrY,$gyrZ,$magX,$magY,$magZ,$pressure,${REAL_MODE + 1}\n"
                    var modeName = ""
                    when (REAL_MODE) {
                        0 -> modeName = "Still"
                        1 -> modeName = "Walk"
                        2 -> modeName = "Run"
                        3 -> modeName = "Bike"
                        4 -> modeName = "Car" //3
                        5 -> modeName = "Bus" //2
                        6 -> modeName = "Train" //1
                        7 -> modeName = "Subway" //0
                    }
                    val path = "${Environment.getExternalStorageDirectory().absolutePath}/tmd_mobile/"
                    val directory = File(path)
                    if (!directory.exists()) {
                        directory.mkdir()
                    }
                    val saveFile = File(path, "$modeName-$TIMESTAMP-NG.csv")
                    saveFile.appendText(content)

                }
            }
        }


    }

}