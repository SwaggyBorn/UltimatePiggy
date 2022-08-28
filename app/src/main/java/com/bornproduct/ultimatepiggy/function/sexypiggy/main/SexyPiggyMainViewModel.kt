package com.bornproduct.ultimatepiggy.function.sexypiggy.main

import androidx.lifecycle.viewModelScope
import com.bornproduct.ultimatepiggy.basic.log.Logger
import com.bornproduct.ultimatepiggy.basic.viewmodel.BaseViewModel
import com.bornproduct.ultimatepiggy.function.sexypiggy.bean.PictureInfoBean
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode.DECRYPTION
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode.ENCRYPTION
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.PictureSdCardUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SexyPiggyMainViewModel: BaseViewModel() {

  companion object{
    private const val HIDE_KEY_NAME = "PIGGY"
  }

  private val sdCardUtil by lazy {
    PictureSdCardUtil()
  }

  fun startEncryption(isEncryption : Boolean){
    viewModelScope.launch {
      //获取所有图片和视频信息
      val allInfoList = withContext(Dispatchers.IO) {
        val pictureList = sdCardUtil.getAllPicture().filter {
          //过滤默认图
          it.name != PictureSdCardUtil.DEFAULT_PICTURE_NAME
        }.toMutableList()
        val videoList = sdCardUtil.getAllVideo()
        pictureList.addAll(videoList)
        pictureList
      }.filter {
        //文件名
        val filename = it.name
        if (filename.isNotEmpty()) {
          val dot: Int = filename.lastIndexOf(".")
          if (dot > -1 && dot < filename.length) {
            it.name = filename.substring(0, dot)
          }
        }
        if (isEncryption) {
          !it.name.contains(HIDE_KEY_NAME)
        }else{
          it.name.contains(HIDE_KEY_NAME)
        }
      }

      Logger.d(getTag(), "搜索需${if (isEncryption) "加密" else "解密"}的图片视频共 ${allInfoList.size} 个")
      allInfoList.forEach {
        encryptionPerPicture(if (isEncryption) ENCRYPTION else DECRYPTION,it)
      }
    }
  }


  /**
   * 单个加密
   * @param mode 加密/解密
   * @param pictureInfoBean 图片信息infoBean
   */
  private suspend fun encryptionPerPicture(mode: AesMode, pictureInfoBean: PictureInfoBean) {
    Logger.e("xxxxxxxxxsfee",pictureInfoBean.name)
    withContext(Dispatchers.IO) {
      //输入流
      val inputStream = sdCardUtil.getInputStream(pictureInfoBean.uri)
      //模式
      when(mode){
        ENCRYPTION -> {
          val outputStream = sdCardUtil.getOutputStream(
            pictureInfoBean, StringBuilder(pictureInfoBean.name).append(
              HIDE_KEY_NAME
            ).toString()
          )
          if(inputStream != null && outputStream != null) {
            AESUtil.encrypt(ENCRYPTION,inputStream,outputStream)
          }
        }

        DECRYPTION ->{
          val outputStream = sdCardUtil.getOutputStream(
            pictureInfoBean, StringBuilder(pictureInfoBean.name).toString().replace(
              HIDE_KEY_NAME,""
            )
          )
          if(inputStream != null && outputStream != null) {
            AESUtil.encrypt(DECRYPTION,inputStream,outputStream)
          }
        }
      }
    }
  }



}