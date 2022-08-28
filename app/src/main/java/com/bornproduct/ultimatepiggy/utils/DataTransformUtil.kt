package com.bornproduct.ultimatepiggy.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DataTransformUtil {

  @SuppressLint("SimpleDateFormat")
  fun timeMillsToYMDHS(timeMills: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return sdf.format(timeMills)
  }

  fun timeMillsToDuration(timeMills: Long): String {
    var dateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA);

    if (timeMills < (60 * 1000L)) {
      dateFormat = SimpleDateFormat("ss", Locale.CHINA)
    } else if (timeMills < (60 * 60 * 1000)) {
      dateFormat = SimpleDateFormat("mm:ss", Locale.CHINA)
    }
    dateFormat.timeZone = TimeZone.getTimeZone("GMT+00:00");
    return dateFormat.format(timeMills)
  }


}