package com.morningstarwang.tmdmobileng.ui.detection

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import android.util.Log.e
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.morningstarwang.tmdmobileng.App
import com.morningstarwang.tmdmobileng.MainActivity
import com.morningstarwang.tmdmobileng.PREDICT_DATA_RECEIVER
import com.morningstarwang.tmdmobileng.receiver.DetectionUIUpdateReceiver
import com.morningstarwang.tmdmobileng.receiver.PredictDataReceiver
import com.morningstarwang.tmdmobileng.utils.FileUtils
import org.jetbrains.anko.layoutInflater

class DetectionViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val context = application
    var confusionMatrix = Array<Array<TextView?>>(9) { arrayOfNulls(9) }
    var matrixHeaders: Array<String> = arrayOf(
        "",
        "静",
        "行",
        "跑",
        "骑",
        "车",
        "交",
        "火",
        "地"
    )

    var uiReceiver: BroadcastReceiver? = null

    init {
        uiReceiver = DetectionUIUpdateReceiver(this)
    }

    fun btnDetectionOnClick(isChecked: Boolean, activity: FragmentActivity?) {
        if (isChecked) {
            App.isPredicting = true
//            val handlerThread = HandlerThread("PREDICT_THREAD")
//            handlerThread.start()
//            val looper = handlerThread.looper
//            val handler = Handler(looper)
            (activity as MainActivity).registerTargetReceiver(PREDICT_DATA_RECEIVER)
        } else {
            App.isPredicting = false
            (activity as MainActivity).unregisterTargetReceiver(PREDICT_DATA_RECEIVER)
        }
    }

    fun loadDataFromLocal(){
        FileUtils.loadObject("0.bin")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerUIUpdateReceiver(){
        context.registerReceiver(
            uiReceiver!!,
            IntentFilter("com.morningstarwang.tmdmobileng.service.SensorService.UPDATE_PREDICTION_UI")
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unRegisterUIUpdateReceiver(){
        context.unregisterReceiver(uiReceiver!!)
    }

    fun loadDataToConfusionMatrix(model: Int) {
        e("loadDataToConfusionMatrix","--")
        //第1-8行且第1-8列为数字
        for (i in 1..8) {
            for (j in 1..8) {
                e("each",  App.confusionValues[3][i][j].toString())
                confusionMatrix[i][j]?.text = App.confusionValues[3][i][j].toString()
            }
        }

    }
}
