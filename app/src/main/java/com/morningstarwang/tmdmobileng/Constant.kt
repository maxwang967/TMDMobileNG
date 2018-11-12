package com.morningstarwang.tmdmobileng

//窗口大小
const val WINDOW_SIZE = 450
//接口地址(列表)
//ICT.新
const val BASE_URL_ICT_NEW = "http://47.95.255.173:5004/"
//ICT.旧
const val BASE_URL_ICT_OLD = "http://47.95.255.173:5001/"
//HTC
const val BASE_URL_HTC = "http://47.95.255.173:5005/"
//HWA
const val BASE_URL_HWA = "http://47.95.255.173:5000/"
//HWB
const val BASE_URL_HWB = "http://47.95.255.173:5003/"

//PredictDataReceiver
const val PREDICT_DATA_RECEIVER = 0x1
//SensorDataReceiver
const val SENSOR_DATA_RECEIVER = 0x2

//真实模式
var REAL_MODE = -1
//识别模型
var DETECTION_MODEL = -1
//时间戳
var TIMESTAMP = 0L

//更新混淆矩阵
const val UPDATE_CONFUSION_MATRIX = 0x1

