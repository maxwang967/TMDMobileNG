package com.morningstarwang.tmdmobileng.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class SensorData(var x: Float, var y: Float, var z: Float) : Parcelable

data class PostData(
    var mLAccList: List<SensorData>?,
    var mAccList: List<SensorData>?,
    var mGyrList: List<SensorData>?,
    var mMagList: List<SensorData>?,
    var mPressureList: List<Float>
)

data class CacheData(
    var confusionValues: ArrayList<Array<Array<Int?>>>,
    var totalAllCountVote: ArrayList<Int>,
    var totalCorrectCountVote: ArrayList<Int>
) : Serializable

data class UpdateData(var versionCode: Int, var description: String, var force: Int, var url: String)