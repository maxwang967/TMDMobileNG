package com.morningstarwang.tmdmobileng.ui.collect

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.morningstarwang.tmdmobileng.App
import com.morningstarwang.tmdmobileng.MainActivity
import com.morningstarwang.tmdmobileng.SENSOR_DATA_RECEIVER
import com.morningstarwang.tmdmobileng.bean.SensorData
import com.morningstarwang.tmdmobileng.receiver.CollectUIUpdateReceiver
import com.morningstarwang.tmdmobileng.service.SensorService
import android.R



class CollectViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val context = application
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
            (activity as MainActivity).registerTargetReceiver(SENSOR_DATA_RECEIVER)
        } else {
            App.isCollecting = false
            activity?.stopService(Intent(activity, SensorService::class.java))
            (activity as MainActivity).unregisterTargetReceiver(SENSOR_DATA_RECEIVER)
        }
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerUIUpdateReceiver(){
        context.registerReceiver(
            uiReceiver!!,
            IntentFilter("com.morningstarwang.tmdmobileng.service.SensorService.UPDATE_COLLECT_UI")
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unRegisterUIUpdateReceiver(){
        context.unregisterReceiver(uiReceiver!!)
    }





}
