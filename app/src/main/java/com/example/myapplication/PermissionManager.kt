package com.example.myapplication

import android.Manifest
import android.app.Activity

class PermissionManager(private val activity: Activity) {
    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE
    )

    fun requestMultiplePermission() {
        activity.requestPermissions(permissionList,1)
    }
}