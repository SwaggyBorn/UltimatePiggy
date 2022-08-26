package com.bornproduct.ultimatepiggy.function.sexypiggy.utils

import android.annotation.SuppressLint
import android.util.Log
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode.DECRYPTION
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.AESUtil.AesMode.ENCRYPTION
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.SecretKeySpec

@SuppressLint("GetInstance")
object AESUtil {

  private val TAG = this.javaClass.simpleName

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
   * 加密/解密
   * @param mode 模式
   * @param content 内容
   */
  fun encryption(mode: AesMode, content: ByteArray): ByteArray? {
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

  enum class AesMode(val value: Int) {
    ENCRYPTION(1),
    DECRYPTION(2)
  }
}