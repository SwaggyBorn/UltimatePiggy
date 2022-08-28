package com.bornproduct.ultimatepiggy.basic.viewmodel

import androidx.lifecycle.ViewModel
import com.bornproduct.ultimatepiggy.basic.BaseView

abstract class BaseViewModel : ViewModel() {
  internal fun getTag() : String{
    return this.javaClass.simpleName
  }
}