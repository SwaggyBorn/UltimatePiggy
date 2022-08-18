package com.bornproduct.ultimatepiggy.basic.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface BaseLifeCycleOwner : LifecycleOwner{

  fun getTag(): String

}