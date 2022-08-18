package com.bornproduct.ultimatepiggy.basic.lifecycle

import androidx.lifecycle.LifecycleOwner

interface BaseLifeCycleAutoClearer {
  fun clear(owner: LifecycleOwner)
}