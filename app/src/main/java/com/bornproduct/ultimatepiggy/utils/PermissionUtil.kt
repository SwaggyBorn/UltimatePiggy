package com.bornproduct.ultimatepiggy.utils

import android.Manifest
import com.bornproduct.ultimatepiggy.basic.BaseActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback

object PermissionUtil {

  fun getReadWritePermission(activity: BaseActivity, allGrantedListener: (Boolean) -> Unit = {}) {
    PermissionX.init(activity)
      .permissions(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
      ).request { allGranted, _, _ ->
        allGrantedListener.invoke(allGranted)
      }
  }

}