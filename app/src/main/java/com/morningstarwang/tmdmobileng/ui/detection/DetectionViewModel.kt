package com.morningstarwang.tmdmobileng.ui.detection

import android.app.Application
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.util.Log.e
import android.widget.TextView
import androidx.lifecycle.*
import com.morningstarwang.tmdmobileng.App
import com.morningstarwang.tmdmobileng.DETECTION_MODEL
import com.morningstarwang.tmdmobileng.receiver.DetectionUIUpdateReceiver

class DetectionViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    val predictResult: MutableLiveData<String> = MutableLiveData()
    val voteResult: MutableLiveData<String> = MutableLiveData()

    val predictCurrentAccuracy: MutableLiveData<String> = MutableLiveData()
    val voteCurrentAccuracy: MutableLiveData<String> = MutableLiveData()

    val predictTotalAccuracy: MutableLiveData<String> = MutableLiveData()
    val voteTotalAccuracy: MutableLiveData<String> = MutableLiveData()

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

    private var uiReceiver: BroadcastReceiver? = null

    init {
        uiReceiver = DetectionUIUpdateReceiver(this)
    }

    fun btnDetectionOnClick(isChecked: Boolean) {
        if (isChecked) {
            App.isPredicting = true
            resetCurrentVariables()
        } else {
            App.isPredicting = false
        }
    }

    private fun resetCurrentVariables() {
        App.currentCorrectCountVote = arrayListOf(0, 0, 0, 0, 0)
        App.currentAllCountVote = arrayListOf(0, 0, 0, 0, 0)
        for (i in 0..4) {
            for (j in 0..8) {
                for (k in 0..8) {
                    App.currentConfusionValues[i][j][k] = 0
                }
            }
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerUIUpdateReceiver() {
        context.registerReceiver(
            uiReceiver!!,
            IntentFilter("com.morningstarwang.tmdmobileng.service.SensorService.UPDATE_PREDICTION_UI")
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unRegisterUIUpdateReceiver() {
        context.unregisterReceiver(uiReceiver!!)
    }

    fun loadDataToConfusionMatrix() {
        val model = DETECTION_MODEL
        var currentCorrectCount = 0
        var totalCorrectCount = 0

        var currentAllCount = 0
        var totalAllCount = 0
        //第1-8行且第1-8列为数字
        for (i in 1..8) {
            for (j in 1..8) {
                if (i == j) {
                    currentCorrectCount += App.currentConfusionValues[model][i][j]!!
                    totalCorrectCount += App.confusionValues[model][i][j]!!
                }
                currentAllCount += App.currentConfusionValues[model][i][j]!!
                totalAllCount += App.confusionValues[model][i][j]!!

                confusionMatrix[i][j]?.text = App.confusionValues[model][i][j].toString()
            }
        }
        var predictCurrentAccuracy = 0f
        var predictTotalAccuracy = 0f
        if (currentAllCount != 0) {
            predictCurrentAccuracy = (currentCorrectCount * 100 / currentAllCount).toFloat()
            e("predictCurrentAccuracy", predictCurrentAccuracy.toString())
        }
        if (totalAllCount != 0) {
            predictTotalAccuracy = (totalCorrectCount * 100 / totalAllCount).toFloat()
            e("predictTotalAccuracy", predictTotalAccuracy.toString())
        }

        this.predictCurrentAccuracy.postValue(String.format("%.2f", predictCurrentAccuracy) + "%")
        this.predictTotalAccuracy.postValue(String.format("%.2f", predictTotalAccuracy) + "%")
        this.predictResult.postValue(App.predictResult[model])

        var voteCurrentAccuracy = 0f
        var voteTotalAccuracy = 0f
        if (App.currentAllCountVote[model] != 0) {
            voteCurrentAccuracy = (App.currentCorrectCountVote[model] * 100 / App.currentAllCountVote[model]).toFloat()
        }
        if (App.totalCorrectCountVote[model] != 0) {
            voteTotalAccuracy = (App.totalCorrectCountVote[model] * 100 / App.totalCorrectCountVote[model]).toFloat()
        }
        this.voteCurrentAccuracy.postValue(String.format("%.2f", voteCurrentAccuracy) + "%")
        this.voteTotalAccuracy.postValue(String.format("%.2f", voteTotalAccuracy) + "%")
        this.voteResult.postValue(App.voteResult[model])
    }
}
