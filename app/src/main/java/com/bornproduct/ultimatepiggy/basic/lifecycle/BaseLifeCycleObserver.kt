package com.bornproduct.ultimatepiggy.basic.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bornproduct.ultimatepiggy.basic.MainApplication

class BaseLifeCycleObserver() : DefaultLifecycleObserver{

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

  }

  override fun onStart(owner: LifecycleOwner) {
    super.onStart(owner)
  }

  override fun onResume(owner: LifecycleOwner) {
    super.onResume(owner)
  }

  override fun onPause(owner: LifecycleOwner) {
    super.onPause(owner)
  }

  override fun onStop(owner: LifecycleOwner) {
    super.onStop(owner)
  }

  override fun onDestroy(owner: LifecycleOwner) {
    super.onDestroy(owner)
  }
}