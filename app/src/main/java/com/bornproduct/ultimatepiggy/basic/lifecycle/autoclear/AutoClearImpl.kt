package com.bornproduct.ultimatepiggy.basic.lifecycle.autoclear

interface AutoClearImpl {
  /**
   * 如果被代理属性实现该接口的话，在清理前会先调用该方法
   */
  fun clear()
}