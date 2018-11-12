package com.morningstarwang.tmdmobileng

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log.e
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.morningstarwang.tmdmobileng.databinding.ActivityMainBinding
import com.morningstarwang.tmdmobileng.receiver.CollectUIUpdateReceiver
import com.morningstarwang.tmdmobileng.receiver.PredictDataReceiver
import com.morningstarwang.tmdmobileng.receiver.SensorDataReceiver
import com.morningstarwang.tmdmobileng.service.SensorService
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var predictDataReceiver: PredictDataReceiver
    private lateinit var sensorDataReceiver: SensorDataReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = Navigation.findNavController(this, R.id.main_nav_fragment)

        //添加以支持汉堡菜单显示
        setSupportActionBar(binding.includedToolbar.toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        //添加以支持侧边栏项目响应事件
        binding.navigationView.setupWithNavController(navController)
        requestPermission()
    }

    //添加以支持汉堡菜单按钮响应事件
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(drawerLayout,
            Navigation.findNavController(this, R.id.main_nav_fragment))
    }

    //添加以支持汉堡菜单返回按钮响应事件
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        stopService(Intent(this, SensorService::class.java))
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @PermissionFail(requestCode = 100)
    fun permissionDenied() {
        toast(getString(R.string.alert_permission_must_be_given))
        requestPermission()
    }


    private fun requestPermission() {
        PermissionGen.with(this)
            .addRequestCode(100)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            .request()
    }



    fun registerTargetReceiver(mode: Int){
        when(mode){
            PREDICT_DATA_RECEIVER -> {
                predictDataReceiver = PredictDataReceiver()
                registerReceiver(predictDataReceiver,
                    IntentFilter("com.morningstarwang.tmdmobileng.service.SensorService.PREDICT")
                    )
            }
            SENSOR_DATA_RECEIVER -> {
                sensorDataReceiver = SensorDataReceiver()
                registerReceiver(
                    sensorDataReceiver,
                    IntentFilter("com.morningstarwang.tmdmobileng.service.SensorService.SAVE_DATA")
                )
            }
        }
    }


    fun unregisterTargetReceiver(mode: Int){
        when(mode){
            PREDICT_DATA_RECEIVER -> this.unregisterReceiver(predictDataReceiver)
            SENSOR_DATA_RECEIVER -> this.unregisterReceiver(sensorDataReceiver)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterReceivers(){
        e("activity-onPause", "onPause")
        unregisterTargetReceiver(PREDICT_DATA_RECEIVER)
        unregisterTargetReceiver(SENSOR_DATA_RECEIVER)
    }







}
