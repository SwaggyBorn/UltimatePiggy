package com.bornproduct.ultimatepiggy.function.sexypiggy.bean

import android.net.Uri
import java.io.Serializable

data class PictureInfoBean(
  var id : Long,
  var uri : Uri,
  var name : String,
  var path : String,
  var mimeType : String,
  var size : Long,
  var modifiedTime : Long

) : Serializable
