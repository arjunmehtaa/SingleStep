package com.example.singlestep.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
    val directory =
        File(context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "CityImages")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val fileName = "city_${System.currentTimeMillis()}.jpg"
    val file = File(directory, fileName)
    return try {
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        stream.flush()
        stream.close()
        file
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun loadBitmapFromFile(context: Context, filePath: String): Bitmap? {
    return try {
        val file = File(filePath)
        if (file.exists()) {
            BitmapFactory.decodeFile(filePath)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
