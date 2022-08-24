package com.bornproduct.ultimatepiggy.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.bornproduct.ultimatepiggy.basic.MainApplication
import com.bornproduct.ultimatepiggy.basic.log.Logger
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class PictureSaveUtil(private val context: Context) {

  companion object {
    private const val TAG = "PermissionUtil"
    const val PICTURE_FILE_DIR_NAME = "SexyPiggy/"
    const val MAX_DEAL_SIZE = 1024
    const val DEFAULT_PICTURE_NAME = "defaultPicture.jpg"
  }

  /**
   *  检查目录并创建默认图
   */
  fun checkFileDir(): File {
    val baseDir = File(
      Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
      ),
      PICTURE_FILE_DIR_NAME
    )
    if (!baseDir.exists()) {
      // 目录不存在 则创建
      baseDir.mkdirs()
      saveDefaultPicture(baseDir)
    }
    return baseDir
  }


  /**
   * 通知图片变更刷新
   * @param fileNameArray 图片文件名数组
   * @param scanListener 刷新回调
   */
  private fun notifyPictureRefresh(
    fileNameArray: Array<String>,
    scanListener: (path: String, uri: Uri) -> Unit = { _, _ -> }
  ) {
    MediaScannerConnection.scanFile(
      context, fileNameArray, null
    ) { path, uri ->
      scanListener.invoke(path, uri)
    }
  }


  /**
   * 创建默认图
   * @param baseDir 根文件夹
   */
  private fun saveDefaultPicture(baseDir: File) {
    val defaultFile = File(baseDir, DEFAULT_PICTURE_NAME)
    val fileOutputStream = FileOutputStream(defaultFile)
    val inputStream = context.assets.open(DEFAULT_PICTURE_NAME)
    val byteArray = ByteArray(MAX_DEAL_SIZE)
    try {
      fileOutputStream.use { outputStream ->
        inputStream.use { inputStream ->
          while (true) {
            val readLen = inputStream.read(byteArray)
            if (readLen == -1) {
              break
            }
            outputStream.write(byteArray, 0, readLen)
          }
        }
      }

      notifyPictureRefresh(arrayOf(defaultFile.toString()))
    } catch (e: Throwable) {
      Logger.e(TAG, "创建默认图异常 ：${e.message}")
    }
  }

}