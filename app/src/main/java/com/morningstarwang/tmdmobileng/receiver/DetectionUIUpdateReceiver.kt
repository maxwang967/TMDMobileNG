package com.morningstarwang.tmdmobileng.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.morningstarwang.tmdmobileng.ui.detection.DetectionViewModel

class DetectionUIUpdateReceiver(var viewModel: DetectionViewModel): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        viewModel.loadDataToConfusionMatrix(intent!!.getIntExtra("model", -1))
    }
}