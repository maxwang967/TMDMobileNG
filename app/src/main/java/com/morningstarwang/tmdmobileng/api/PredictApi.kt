package com.morningstarwang.tmd_mobile_next.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PredictApi {
    @POST("get_4nl_mode")
    fun predictICTNew(@Body postData: RequestBody): Call<ResponseBody>


    @POST("get_4_mode")
    fun predictICTOld(@Body postData: RequestBody): Call<ResponseBody>

    @POST("get_8_htc_mode")
    fun predictHTC(@Body postData: RequestBody): Call<ResponseBody>

    @POST("get_8_mode")
    fun predictHuaweiA(@Body postData: RequestBody): Call<ResponseBody>

    @POST("get_8_mode_pf")
    fun predictHuaweiB(@Body postData: RequestBody): Call<ResponseBody>
}