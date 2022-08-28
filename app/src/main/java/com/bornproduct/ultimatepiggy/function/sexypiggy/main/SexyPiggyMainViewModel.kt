package com.bornproduct.ultimatepiggy.function.sexypiggy.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bornproduct.ultimatepiggy.basic.log.Logger
import com.bornproduct.ultimatepiggy.basic.viewmodel.BaseViewModel
import com.bornproduct.ultimatepiggy.function.sexypiggy.bean.PictureDealBean
import com.bornproduct.ultimatepiggy.function.sexypiggy.bean.PictureInfoBean
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode.DECRYPTION
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode.ENCRYPTION
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.PictureSdCardUtil
import com.bornproduct.ultimatepiggy.utils.DataTransformUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SexyPiggyMainViewModel: BaseViewModel() {

  companion object{
    private const val HIDE_KEY_NAME = "PIGGY"
  }

  private val dealingBean : MutableLiveData<PictureDealBean> by lazy {
    MutableLiveData<PictureDealBean>()
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
      dealingBean.value = PictureDealBean()
      dealingBean.value?.apply {
        total = allInfoList.size
        startTime = System.currentTimeMillis()
        allInfoList.forEach{
          totalSize += it.size
        }
      }
      allInfoList.forEach { pictureInfoBean ->
        encryptionPerPicture(
          if (isEncryption) ENCRYPTION else DECRYPTION,
          pictureInfoBean
        ) { isSuccess ->
          dealingBean.value?.apply {
            val notTime = System.currentTimeMillis()
            duration = if (notTime > startTime){
              notTime - startTime
            }else{
              duration
            }
            current = dealingBean.value?.current!! + 1
            if (isSuccess){
              sdCardUtil.deleteByUri(pictureInfoBean.path,pictureInfoBean.uri)
            }
            Logger.d(getTag(), "目前进度： $current / $total   目前时长：${DataTransformUtil.timeMillsToDuration(duration)}")
          }
        }
      }
    }
  }


  /**
   * 单个加密
   * @param mode 加密/解密
   * @param pictureInfoBean 图片信息infoBean
   */
  private suspend fun encryptionPerPicture(
    mode: AesMode,
    pictureInfoBean: PictureInfoBean,
    refreshListener: (Boolean) -> Unit
  ) {
    withContext(Dispatchers.IO) {
      //输入流
      val inputStream = sdCardUtil.getInputStream(pictureInfoBean.uri)
      //模式
      when (mode) {
        ENCRYPTION -> {
          val outputStream = sdCardUtil.getOutputStream(
            pictureInfoBean, StringBuilder(pictureInfoBean.name).append(
              HIDE_KEY_NAME
            ).toString()
          )
          if (inputStream != null && outputStream != null) {
            refreshListener(AESUtil.encrypt(ENCRYPTION, inputStream, outputStream))
          }else{
            refreshListener(false)
          }
        }

        DECRYPTION -> {
          val outputStream = sdCardUtil.getOutputStream(
            pictureInfoBean, StringBuilder(pictureInfoBean.name).toString().replace(
              HIDE_KEY_NAME, ""
            )
          )
          if (inputStream != null && outputStream != null) {
            refreshListener(AESUtil.encrypt(DECRYPTION, inputStream, outputStream))
          }else{
            refreshListener(false)
          }
        }
      }
    }
  }



}