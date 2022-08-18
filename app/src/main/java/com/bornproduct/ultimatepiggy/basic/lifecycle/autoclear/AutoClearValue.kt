package com.bornproduct.ultimatepiggy.basic.lifecycle.autoclear

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bornproduct.ultimatepiggy.basic.lifecycle.BaseLifeCycleAutoClearer
import com.bornproduct.ultimatepiggy.basic.lifecycle.BaseLifeCycleOwner
import java.io.Closeable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 当 [LifecycleOwner] 状态处于 [DESTROYED] 时会清理变量
 *
 * 最好不要在 [DESTROYED] 状态使用该变量，虽然不会导致异常，但是会导致变量脱离生命周期管理
 *
 * 如果 [T] 是 [Clearable] 或者 [Clearable] 的实例，则 [Clearable.clear] 方法会在变量被清理之前调用
 * 如果 [T] 是 [Closeable] 或者 [AutoCloseable] 的实例，则 [Closeable.close] 方法会在变量被清理之前调用
 * @param valueProvider  变量的初始化器,get变量时，如果变量值为null,则会调用该初始化器进行初始化
 */

class AutoClearValue<T>(private var valueProvider: () -> T) :
  ReadWriteProperty<BaseLifeCycleOwner, T> {
  private var value: T? = null
  private val lifecycleObserver = object : DefaultLifecycleObserver {
    @SuppressLint("ObsoleteSdkInt")
    override fun onDestroy(owner: LifecycleOwner) {
      // 当生命周期变化为 onDestroy 时，执行清理操作
      owner.lifecycle.removeObserver(this)
      value?.let { it ->
        if (it is BaseLifeCycleAutoClearer) {
          try {
            it.clear(owner)
          } catch (e: Throwable) {
            Log.e("AutoClearedValue", "clear error: ", e)
          }
        } else if (it is AutoClearImpl) {
          try {
            it.clear()
          } catch (e: Throwable) {
            Log.e("AutoClearedValue", "clear error: ", e)
          }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
          if (it is AutoCloseable) {
            try {
              it.close()
            } catch (e: Throwable) {
              Log.e("AutoClearedValue", "close error: ", e)
            }
          }
        } else {
          if (it is Closeable) {
            try {
              it.close()
            } catch (e: Throwable) {
              Log.e("AutoClearedValue", "close error: ", e)
            }
          }
        }
      }
      value = null
    }
  }

  override fun getValue(thisRef: BaseLifeCycleOwner, property: KProperty<*>): T {
    value?.let { return it }
    val lifecycle =
      if (thisRef is Fragment) thisRef.viewLifecycleOwner.lifecycle else thisRef.lifecycle
    // 添加生命周期监听
    lifecycle.addObserver(lifecycleObserver)
    return valueProvider.invoke().also { value = it }
  }

  override fun setValue(thisRef: BaseLifeCycleOwner, property: KProperty<*>, value: T) {
    val lifecycle =
      if (thisRef is Fragment) thisRef.viewLifecycleOwner.lifecycle else thisRef.lifecycle
    lifecycle.addObserver(lifecycleObserver)
    this.value = value
  }
}

