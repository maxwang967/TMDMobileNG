package com.morningstarwang.tmdmobileng.utils

import android.os.Environment
import java.io.*

object FileUtils {
    fun saveObject(obj: Any, fileName: String) {
        runOnIoThread {
            var path = Environment.getExternalStorageDirectory().absolutePath + "/"
            path += "tmd_mobile/"
            val os = FileOutputStream(File(path + fileName))
            val oos = ObjectOutputStream(os)
            oos.writeObject(obj)
            oos.flush()
            oos.close()
        }
    }

    fun loadObject(fileName: String): Any? {
        var path = Environment.getExternalStorageDirectory().absolutePath + "/"
        path += "tmd_mobile/"
        if (!File(path, fileName).exists()) {
            return null
        }
        val istream = FileInputStream(path + fileName)
        val ois = ObjectInputStream(istream)
        val obj = ois.readObject()
        ois.close()
        return obj
    }

    fun deleteFile(fileName: String) {
        var path = Environment.getExternalStorageDirectory().absolutePath + "/"
        path += "tmd_mobile/"
        val deleteFile = File(path + fileName)
        if (deleteFile.exists()) {
            deleteFile.delete()
        }
    }
}