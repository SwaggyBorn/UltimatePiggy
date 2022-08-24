package com.bornproduct.ultimatepiggy.basic

import android.os.Bundle
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.bornproduct.ultimatepiggy.basic.lifecycle.BaseLifeCycleObserver
import com.bornproduct.ultimatepiggy.basic.lifecycle.BaseLifeCycleOwner

abstract class BaseActivity() : AppCompatActivity(), BaseView, BaseLifeCycleOwner {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayoutId())

    //添加lifeCycle观察者
    lifecycle.addObserver(BaseLifeCycleObserver(getTag()))

  }

  override fun getTag(): String = this::class.java.simpleName

  abstract fun getLayoutId(): Int

}