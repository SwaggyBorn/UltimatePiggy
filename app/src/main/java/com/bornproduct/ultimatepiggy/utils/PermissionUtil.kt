package com.bornproduct.ultimatepiggy.utils

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import com.bornproduct.ultimatepiggy.basic.BaseActivity
import com.permissionx.guolindev.PermissionX

object PermissionUtil {

  fun getReadWritePermission(activity: BaseActivity, allGrantedListener: (Boolean) -> Unit = {}) {
    PermissionX.init(activity)
      .permissions(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
      ).request { allGranted, _, _ ->
        allGrantedListener.invoke(allGranted)
        if (VERSION.SDK_INT >= VERSION_CODES.R && allGranted) {
          if (!Environment.isExternalStorageManager()) {
            activity.startActivity(Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
              data = Uri.fromParts("package", activity.packageName, null)
              flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
          }
        }
      }
  }

}