package com.bornproduct.ultimatepiggy.function.sexypiggy.utils

import android.annotation.SuppressLint
import android.util.Log
import com.bornproduct.ultimatepiggy.basic.log.Logger
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode.DECRYPTION
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode.ENCRYPTION
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@SuppressLint("GetInstance")
object AESUtil {

  private val TAG = this.javaClass.simpleName

  //最大缓存空间
  const val MAX_DEAL_SIZE = 1024 * 1024 * 2
  //AES 秘钥key，必须为16位
  private const val AES_KEY = "SexyPiggy7777777"
  //Transformation
  private const val TRANSFORMATION = "AES/ECB/NoPadding"
  //AES 加密方式
  private val KEY_SPEC = SecretKeySpec(AES_KEY.toByteArray(charset("UTF-8")), "AES")

  //加密器
  private val cipherEn :Cipher by lazy {
    Cipher.getInstance(TRANSFORMATION).let {
      it.init(ENCRYPTION.value, KEY_SPEC)
      it
    }
  }

  //解密器
  private val cipherDe :Cipher by lazy {
    Cipher.getInstance(TRANSFORMATION).let {
      it.init(DECRYPTION.value, KEY_SPEC)
      it
    }
  }

  /**
   * 加密/解密(私有)
   * @param mode 模式
   * @param content 内容
   */
  private fun encryptInternal(mode: AesMode, content: ByteArray): ByteArray? {
    try {
      return when (mode) {
        ENCRYPTION -> {
          cipherEn.doFinal(content)
        }
        DECRYPTION -> {
          cipherDe.doFinal(content)
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "${if (mode == ENCRYPTION) "加密" else "解密"} 文件错误:" + e.message)
    }
    return null
  }

  fun encrypt(mode: AesMode, inputStream: InputStream, outputStream: OutputStream): Boolean {
    val buff = ByteArray(MAX_DEAL_SIZE)
    var buffResult: ByteArray?
    return try {
      while (inputStream.read(buff) != -1) {
        buffResult = encryptInternal(mode, buff)
        if (buffResult != null) {
          outputStream.write(buffResult)
        }
      }
      true
    } catch (e: IOException) {
      e.printStackTrace()
      Logger.e(TAG, "加密/解密 文件错误:" + e.message)
      false
    }
  }

  enum class AesMode(val value: Int) {
    ENCRYPTION(1),
    DECRYPTION(2)
  }
}