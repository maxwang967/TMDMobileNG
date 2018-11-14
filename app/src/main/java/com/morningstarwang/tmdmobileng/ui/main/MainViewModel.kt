package com.morningstarwang.tmdmobileng.ui.main

import androidx.lifecycle.MutableLiveData
import com.morningstarwang.tmdmobileng.ui.BaseViewModel

class MainViewModel : BaseViewModel() {
    val announcementText = MutableLiveData<String>()

    fun setAnnouncementText() {
        val tempAnnouncement = """欢迎参与新版本测试工作，操作流程如下：
(自动更新模块暂未开启)
1. 左上角按钮为菜单按钮，可以选择进入主页、数据采集和模式识别
2. 在本页面(主页)选择当前模式，并点击确认
3. 在数据采集页面，点击开启采集
4. 在模式识别页面，点击开始识别
5. 模式识别不需要重复开启，程序默认开启全部识别工作
6. 切换模式识别页面上方选项卡以查看识别结果"""
        announcementText.postValue(tempAnnouncement)
    }
}
