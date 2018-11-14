package com.morningstarwang.tmdmobileng.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class DownloadReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE){
            val intentAPK = Intent(Intent.ACTION_VIEW)
            val output = File(Environment.getExternalStorageDirectory().absolutePath + "/tmd_mobile/ng.apk")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                intentAPK.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val apkUri = FileProvider.getUriForFile(context!!, "com.morningstarwang.tmdmobileng", output)
                intentAPK.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentAPK.setDataAndType(apkUri, "application/vnd.android.package-archive")
            } else {
                intentAPK.setDataAndType(
                    Uri.fromFile(output),
                    "application/vnd.android.package-archive")
            }
            context?.startActivity(intentAPK)
        }
    }
}