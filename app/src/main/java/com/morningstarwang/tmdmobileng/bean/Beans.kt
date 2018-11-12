package com.morningstarwang.tmdmobileng.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SensorData(var x: Float, var y: Float, var z: Float): Parcelable

@Parcelize
data class SensorCollection(var lacc: SensorData, var acc: SensorData, var gyr: SensorData, var mag: SensorData, var pressure: Float): Parcelable

data class PostData(var mLAccList:List<SensorData>?, var mAccList:List<SensorData>?, var mGyrList:List<SensorData>?, var mMagList:List<SensorData>?, var mPressureList: List<Float>)