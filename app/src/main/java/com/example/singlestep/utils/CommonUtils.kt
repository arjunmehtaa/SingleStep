package com.example.singlestep.utils

import java.io.File;
import android.util.Log
import java.io.FileWriter

fun onLoading() {
    Log.i("Loading", "onLoading()")
}

fun onLoadingFailure() {
    Log.i("Loading Failed", "onLoadingFailure()")
}

fun writeToFile(filename: String, content: String)
    = File(filename).writeText(content)

fun readFromFile(filename:String):String
    = File(filename).readText()