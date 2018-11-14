package com.morningstarwang.tmdmobileng

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log.e
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.morningstarwang.tmdmobileng.bean.CacheData
import com.morningstarwang.tmdmobileng.bean.UpdateData
import com.morningstarwang.tmdmobileng.databinding.ActivityMainBinding
import com.morningstarwang.tmdmobileng.receiver.DownloadReceiver
import com.morningstarwang.tmdmobileng.receiver.SensorDataReceiver
import com.morningstarwang.tmdmobileng.service.SensorService
import com.morningstarwang.tmdmobileng.utils.FileUtils
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import org.jetbrains.anko.activityUiThreadWithContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.io.File
import java.net.URL

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sensorDataReceiver: SensorDataReceiver

    private inline fun <reified T : Any> fromJson(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }

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
        registerReceiver(
            DownloadReceiver(),
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        loadLocalData()
    }

    private fun loadLocalData() {
        try {
            val previousData = FileUtils.loadObject("tmd_ng_cache.bin")
            App.confusionValues = (previousData as CacheData).confusionValues
            App.totalCorrectCountVote = previousData.totalCorrectCountVote
            App.totalAllCountVote = previousData.totalAllCountVote
        } catch (e: Exception) {
            App.confusionValues = App.confusionValues
            App.totalCorrectCountVote = App.totalCorrectCountVote
            App.totalAllCountVote = App.totalAllCountVote
            e("bin not found:", e.message)
        }
    }

    //添加以支持汉堡菜单按钮响应事件
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            drawerLayout,
            Navigation.findNavController(this, R.id.main_nav_fragment)
        )
    }

    //添加以支持汉堡菜单返回按钮响应事件
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
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

    @PermissionSuccess(requestCode = 100)
    fun checkForUpdate() {
        doAsync {
            val apkFile = File(Environment.getExternalStorageDirectory().absolutePath + "/tmd_mobile/ng.apk")
            if (apkFile.exists()) {
                apkFile.delete()
            }
            val url = URL(UPDATE_URL)
            val updateInfo = try {
                val content = url.readText()
                e("content", content)
                val updateData: UpdateData = fromJson(content)
                e("updateData=", updateData.toString())
                updateData
            } catch (e: Exception) {
                null
            }
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            e("versionCode", PackageInfoCompat.getLongVersionCode(packageInfo).toString())
            val versionCode = PackageInfoCompat.getLongVersionCode(packageInfo)
            if (versionCode < updateInfo?.versionCode!!.toLong()) {
                e("update", "has new version")
                activityUiThreadWithContext {
                    val context = this
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.software_update))
                        setMessage("我们做了以下工作：\n" + updateInfo.description)
                        if (updateInfo.force == 0) {
                            setNegativeButton("暂不更新") { _, _ ->
                            }
                        }
                        setPositiveButton("立即更新") { _, _ ->
                            downloadAPK(context, updateInfo)
                        }
                        setCancelable(false)
                    }.create().show()
                }
            }
        }
    }

    private fun downloadAPK(context: Context, updateInfo: UpdateData) {

        val request = try {
            val request = DownloadManager.Request(Uri.parse(updateInfo.url))
            request
        } catch (e: Exception) {
            e("update error:", e.message)
            null
        }
        request?.apply {
            setTitle("交通模式识别NG软件更新")
            setDescription(updateInfo.description)
            allowScanningByMediaScanner()
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir("/tmd_mobile", "ng.apk")
        }
        val manager = context.applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        setProgressDialog(context)
    }

    private fun setProgressDialog(context: Context) {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = getString(R.string.alert_downloading_apk)
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 14F
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(ll)

        val dialog = builder.create()
        dialog.show()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog?.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog?.window?.attributes = layoutParams
        }
    }


    private fun requestPermission() {
        PermissionGen.with(this)
            .addRequestCode(100)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .request()
    }


    fun registerTargetReceiver(mode: Int) {
        when (mode) {
            SENSOR_DATA_RECEIVER -> {
                sensorDataReceiver = SensorDataReceiver()
                registerReceiver(
                    sensorDataReceiver,
                    IntentFilter("com.morningstarwang.tmdmobileng.service.SensorService.SAVE_DATA")
                )
            }
        }
    }


    fun unregisterTargetReceiver(mode: Int) {
        when (mode) {
            SENSOR_DATA_RECEIVER -> this.unregisterReceiver(sensorDataReceiver)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterReceivers() {
        e("activity-onPause", "onPause")
        unregisterTargetReceiver(PREDICT_DATA_RECEIVER)
        unregisterTargetReceiver(SENSOR_DATA_RECEIVER)
    }


}
