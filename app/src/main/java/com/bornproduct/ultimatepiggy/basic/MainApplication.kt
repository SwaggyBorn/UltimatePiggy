package com.bornproduct.ultimatepiggy.basic

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.bornproduct.ultimatepiggy.R
import com.bornproduct.ultimatepiggy.basic.lifecycle.BaseLifeCycleObserver

class MainApplication : Application() {



  override fun onCreate() {
    super.onCreate()
    //context赋值
    mContext = this

    //配置Application层的lifecycle
    ProcessLifecycleOwner.get().lifecycle.addObserver(BaseLifeCycleObserver(MainApplication::class.java.simpleName))
  }

  companion object {
    var mContext: Application? = null
    fun getContext(): Context {
      return mContext!!
    }
  }



}