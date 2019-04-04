package com.morningstarwang.tmdmobileng

//窗口大小
const val WINDOW_SIZE = 450
//投票队列长度
const val VOTE_QUEUE_LENGTH = 10
//接口地址(列表)
//ICT.新
const val BASE_URL_ICT_NEW = "http://47.95.255.173:5004/"
//ICT.旧
const val BASE_URL_ICT_OLD = "http://47.95.255.173:5001/"
//HTC
const val BASE_URL_HTC = "http://47.95.255.173:5005/"
//HWA
const val BASE_URL_HWA = "http://47.95.255.173:5000/"
//TODO Change HWB to online learning
const val BASE_URL_HWB = "http://47.95.255.173:8000/tmd-v1/"
//DJANGO
const val BASE_URL_DJANGO = "http://47.95.255.173:8000/tmd-v1/"

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

//GPS信息
var altitude: Double = -1.0
var accuracy: Float = -1f
var bearing: Float = -1f
var latitude: Double = -1.0
var longitude: Double = -1.0
var speed: Float = -1f
var time: Long = -1L
var elapsedRealtimeNanos: Long = -1L
var provider: String = "N/A"

//更新URL
const val UPDATE_URL = "https://service.morningstarwang.com/temp/ict/tmd_app/update.json"
//公告URL
const val ANNOUCEMENT_URL = "https://service.morningstarwang.com/temp/ict/tmd_app/announcement.md"

const val NOTIFICATION_CHANNEL_ID = "5000"
const val NOTIFICATION_CHANNEL_NAME = "TMD_MOBILE_NG"
const val NOTIFICATION_CHANNEL_DESC = "此服务是交通模式识别NG的核心识别服务"
const val NOTIFICATION_ID = 5001

