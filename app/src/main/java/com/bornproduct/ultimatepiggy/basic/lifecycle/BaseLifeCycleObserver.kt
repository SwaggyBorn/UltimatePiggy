package com.bornproduct.ultimatepiggy.basic.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bornproduct.ultimatepiggy.basic.BaseView
import com.bornproduct.ultimatepiggy.basic.MainApplication
import com.bornproduct.ultimatepiggy.basic.log.Logger

class BaseLifeCycleObserver(private val tag : String) : DefaultLifecycleObserver{

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)
    printLog("onCreate()")
  }

  override fun onStart(owner: LifecycleOwner) {
    super.onStart(owner)
    printLog("onStart()")
  }

  override fun onResume(owner: LifecycleOwner) {
    super.onResume(owner)
    printLog("onResume()")
  }

  override fun onPause(owner: LifecycleOwner) {
    super.onPause(owner)
    printLog("onPause()")
  }

  override fun onStop(owner: LifecycleOwner) {
    super.onStop(owner)
    printLog("onStop()")
  }

  override fun onDestroy(owner: LifecycleOwner) {
    super.onDestroy(owner)
    printLog("onDestroy()")
  }

  private fun printLog(status: String) {
      Logger.d(tag, status)

  }
}