package com.morningstarwang.tmdmobileng.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.morningstarwang.tmdmobileng.bean.SensorData
import com.morningstarwang.tmdmobileng.ui.collect.CollectViewModel

/**
 * 处理Service广播发送的每一条数据，更新LiveData以更新UI
 */
class CollectUIUpdateReceiver(var viewModel: CollectViewModel) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val receivedLAcc: SensorData? = intent?.getParcelableExtra("lacc")
        val receivedAcc: SensorData? = intent?.getParcelableExtra("acc")
        val receivedGyr: SensorData? = intent?.getParcelableExtra("gyr")
        val receivedMag: SensorData? = intent?.getParcelableExtra("mag")
        val receivedPressure: Float? = intent?.getFloatExtra("pressure", 0f)
        this.viewModel.lacc.postValue(receivedLAcc)
        this.viewModel.acc.postValue(receivedAcc)
        this.viewModel.gyr.postValue(receivedGyr)
        this.viewModel.mag.postValue(receivedMag)
        this.viewModel.pressure.postValue(receivedPressure)

    }
}