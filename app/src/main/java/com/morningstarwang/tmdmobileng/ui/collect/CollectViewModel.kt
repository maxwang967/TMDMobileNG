package com.morningstarwang.tmdmobileng.ui.collect

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.morningstarwang.tmdmobileng.App
import com.morningstarwang.tmdmobileng.bean.SensorData
import com.morningstarwang.tmdmobileng.receiver.CollectUIUpdateReceiver
import com.morningstarwang.tmdmobileng.receiver.SensorDataReceiver
import com.morningstarwang.tmdmobileng.service.SensorService
import java.util.ArrayList

class CollectViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    val context = application
    val lacc: MutableLiveData<SensorData> = MutableLiveData()
    val acc: MutableLiveData<SensorData> = MutableLiveData()
    val gyr: MutableLiveData<SensorData> = MutableLiveData()
    val mag: MutableLiveData<SensorData> = MutableLiveData()
    val pressure: MutableLiveData<Float> = MutableLiveData()
    var uiReceiver: BroadcastReceiver? = null

    init {
        uiReceiver = CollectUIUpdateReceiver(this)
    }

    fun btnCollectOnClick(isChecked: Boolean, activity: FragmentActivity?) {
        if (isChecked) {
            App.isCollecting = true
            activity?.startService(Intent(activity, SensorService::class.java))
            val handlerThread = HandlerThread("SAVE_DATA")
            handlerThread.start()
            val looper = handlerThread.looper
            val handler = Handler(looper)
            activity?.registerReceiver(
                SensorDataReceiver(),
                IntentFilter("com.morningstarwang.tmdmobileng.service.SensorService.SAVE_DATA"),
                null,
                handler
            )
        } else {
            App.isCollecting = false
            activity?.stopService(Intent(activity, SensorService::class.java))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerUIUpdateReceiver(){
        context?.registerReceiver(
            uiReceiver,
            IntentFilter("com.morningstarwang.tmdmobileng.service.SensorService.UPDATE_UI")
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unRegisterUIUpdateReceiver(){
        context?.unregisterReceiver(uiReceiver)
    }





}
