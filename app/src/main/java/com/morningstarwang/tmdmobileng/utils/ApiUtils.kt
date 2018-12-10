package com.morningstarwang.tmdmobileng.utils

import android.util.Log.e
import com.morningstarwang.tmdmobileng.*
import com.morningstarwang.tmdmobileng.api.Api
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

object ApiUtils {

    private var retrofitICTNew: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_ICT_NEW)
        .build()
    private var retrofitICTOld: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_ICT_OLD)
        .build()
    private var retrofitHTC: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_HTC)
        .build()
    private var retrofitHuaweiA: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_HWA)
        .build()
    private var retrofitHuaweiB: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_HWB)
        .build()
    private var retrofitDjango: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_DJANGO)
        .build()

    fun predict(model: Int, bodyContent: String): Call<ResponseBody>? {
        e("predict.Thread", Thread.currentThread().name)
        val service = when (model) {
            0 -> retrofitICTNew.create(Api::class.java)
            1 -> retrofitICTOld.create(Api::class.java)
            2 -> retrofitHTC.create(Api::class.java)
            3 -> retrofitHuaweiA.create(Api::class.java)
            4 -> retrofitHuaweiB.create(Api::class.java)
            else -> {
                null
            }
        }
        val body = RequestBody.create(MediaType.parse("application/json"), bodyContent)
        return when (model) {
            0 -> service?.predictICTNew(body)
            1 -> service?.predictICTOld(body)
            2 -> service?.predictHTC(body)
            3 -> service?.predictHuaweiA(body)
            4 -> service?.predictHuaweiB(body)
            else -> {
                null
            }
        }
    }

    fun login(username: String, password: String): Call<ResponseBody>? {
        val service = retrofitDjango.create(Api::class.java)
        val body =
            RequestBody.create(MediaType.parse("application/json"), "{\"username\":$username, \"password\": $password}")
        return service.login(body)
    }

}