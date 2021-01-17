package com.leokorol.testlove.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun Context.isConnectedToInternet(): Boolean {
    val cm = (getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager)
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}

fun Context.showToast(message: String) {
    val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}
