package com.morningstarwang.tmdmobileng.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.IBinder
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager.*
import android.os.Bundle
import android.os.Parcelable
import android.os.PowerManager
import android.os.PowerManager.PARTIAL_WAKE_LOCK
import android.util.Log.e
import android.util.Log.i
import com.morningstarwang.tmdmobileng.WINDOW_SIZE
import com.morningstarwang.tmdmobileng.bean.SensorData
import java.util.*
import kotlin.collections.ArrayList


class SensorService : Service() {


    private var sensorManager: android.hardware.SensorManager? =
        null
    private var lAccSensor: Sensor? = null
    private var accSensor: Sensor? = null
    private var gyrSensor: Sensor? = null
    private var magSensor: Sensor? = null
    private var pressureSensor: Sensor? = null

    private var laccList = mutableListOf<SensorData>()
    private var accList = mutableListOf<SensorData>()
    private var gyrList = mutableListOf<SensorData>()
    private var magList = mutableListOf<SensorData>()
    private var pressureList = mutableListOf<Float>()


    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        i("SensorService", "onCreate")
        e("Service-Thread", Thread.currentThread().name)
        if (sensorManager == null) {
            sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        }

        lAccSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        accSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyrSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        pressureSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)

        sensorManager?.registerListener(sensorListener, lAccSensor, SENSOR_DELAY_GAME)
        sensorManager?.registerListener(sensorListener, accSensor, SENSOR_DELAY_GAME)
        sensorManager?.registerListener(sensorListener, gyrSensor, SENSOR_DELAY_GAME)
        sensorManager?.registerListener(sensorListener, magSensor, SENSOR_DELAY_GAME)
        sensorManager?.registerListener(sensorListener, pressureSensor, SENSOR_DELAY_FASTEST)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PARTIAL_WAKE_LOCK, SensorService::class.java.name)
        wakeLock!!.acquire()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (sensorManager != null) {
            sensorManager!!.unregisterListener(sensorListener)
            sensorListener = null
        }

        if (wakeLock != null) {
            wakeLock!!.release()
            wakeLock = null
        }
    }


    private var sensorListener: SensorEventListener? = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            if (laccList.size > 0 &&
                accList.size > 0 &&
                gyrList.size > 0 &&
                magList.size > 0 &&
                pressureList.size > 0
            ){
                val intent = Intent().apply {
                    putExtra("lacc", laccList[laccList.size - 1])
                    putExtra("acc", accList[accList.size - 1])
                    putExtra("gyr", gyrList[gyrList.size - 1])
                    putExtra("mag", magList[magList.size - 1])
                    putExtra("pressure", pressureList[pressureList.size - 1])
                    action = "com.morningstarwang.tmdmobileng.service.SensorService.UPDATE_UI"
                }
                sendBroadcast(intent)
            }
            if (laccList.size >= WINDOW_SIZE &&
                accList.size >= WINDOW_SIZE &&
                gyrList.size >= WINDOW_SIZE &&
                magList.size >= WINDOW_SIZE &&
                pressureList.size >= WINDOW_SIZE
            ) {
                i("laccListContent=", laccList.toList().toString())
                for (i in 0 until pressureList.size - 450) {
                    pressureList.removeAt(i)
                }
                val bundle = Bundle().apply {
                    putParcelableArrayList("laccList", ArrayList<Parcelable>(laccList))
                    putParcelableArrayList("accList", ArrayList<Parcelable>(accList))
                    putParcelableArrayList("gyrList", ArrayList<Parcelable>(gyrList))
                    putParcelableArrayList("magList", ArrayList<Parcelable>(magList))
                    putFloatArray("pressureList", pressureList.toFloatArray())
                }
                val intent = Intent().apply {
                    putExtra("window_data", bundle)
                    action = "com.morningstarwang.tmdmobileng.service.SensorService.SAVE_DATA"
                }
                sendBroadcast(intent)
                laccList.clear()
                accList.clear()
                gyrList.clear()
                magList.clear()
                i("laccListSize=", laccList.size.toString())
                i("accListSize=", accList.size.toString())
                i("gyrListSize=", gyrList.size.toString())
                i("magListSize=", magList.size.toString())
                i("pressureListSize=", pressureList.size.toString())
            }
            when (event?.sensor?.type) {
                Sensor.TYPE_LINEAR_ACCELERATION -> {
                    val data = SensorData(event.values[0], event.values[1], event.values[2])
                    laccList.add(data)
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    val data = SensorData(event.values[0], event.values[1], event.values[2])
                    accList.add(data)
                }
                Sensor.TYPE_GYROSCOPE -> {
                    val data = SensorData(event.values[0], event.values[1], event.values[2])
                    gyrList.add(data)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    val data = SensorData(event.values[0], event.values[1], event.values[2])
                    magList.add(data)
                }
                Sensor.TYPE_PRESSURE -> {
                    val data = event.values[0]
                    pressureList.add(data)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }
    }
}