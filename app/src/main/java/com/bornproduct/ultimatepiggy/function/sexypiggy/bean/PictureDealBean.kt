package com.bornproduct.ultimatepiggy.function.sexypiggy.bean

import java.io.Serializable

data class PictureDealBean(
  var total: Int = 0,
  var current: Int = 0,
  var startTime: Long = 0,
  var duration: Long = 0,
  var totalSize: Long = 0
) : Serializable {
}