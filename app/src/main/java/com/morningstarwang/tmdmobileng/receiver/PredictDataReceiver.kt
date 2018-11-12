package com.morningstarwang.tmdmobileng.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log.e
import android.widget.ToggleButton
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.morningstarwang.tmdmobileng.App.Companion.confusionValues
import com.morningstarwang.tmdmobileng.DETECTION_MODEL
import com.morningstarwang.tmdmobileng.R
import com.morningstarwang.tmdmobileng.REAL_MODE
import com.morningstarwang.tmdmobileng.WINDOW_SIZE
import com.morningstarwang.tmdmobileng.bean.PostData
import com.morningstarwang.tmdmobileng.bean.SensorData
import com.morningstarwang.tmdmobileng.ui.detection.DetectionFragment
import com.morningstarwang.tmdmobileng.utils.ApiUtils
import okhttp3.ResponseBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Response

class PredictDataReceiver: BroadcastReceiver() {

    private  var context: Context? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context



    }

}