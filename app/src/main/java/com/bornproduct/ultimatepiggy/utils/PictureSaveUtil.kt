package com.bornproduct.ultimatepiggy.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import com.bornproduct.ultimatepiggy.basic.log.Logger
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception

class PictureSaveUtil(private val context: Context) {

  companion object {
    private const val TAG = "PermissionUtil"
    const val PICTURE_FILE_DIR_NAME = "/SexyPiggy "
    const val MAX_DEAL_SIZE = 1024
    const val DEFAULT_PICTURE_NAME = "defaultPicture"
    const val MIME_TYPE = "image/png"
  }

  /**
   *  检查目录并创建默认图
   */
  fun checkFileDir(): File {
    val baseDir = File(getFileDirPath())
    if (!baseDir.exists()) {
      // 目录不存在 则创建
      baseDir.mkdirs()
      createDefaultFile()
    }
    return baseDir
  }

  /**
   * 获取本app相册文件夹绝对路径
   */
  private fun getFileDirPath(): String {
    return File(
      Environment.getExternalStoragePublicDirectory(
        DIRECTORY_PICTURES
      ),
      PICTURE_FILE_DIR_NAME
    ).path
  }

  /**
   * 通知图片变更刷新
   * @param fileNameArray 图片文件名数组(Environment.getExternalStoragePublicDirectory下)
   * @param scanListener 刷新回调
   */
  private fun notifyPictureRefresh(
    fileNameArray: Array<String>,
    scanListener: (path: String?, uri: Uri?) -> Unit = { _, _ -> }
  ) {
    MediaScannerConnection.scanFile(
      context, fileNameArray, null
    ) { path, uri ->
      scanListener.invoke(path, uri)
    }
  }

  /**
   * 创建默认图
   */
  private fun createDefaultFile() {
    val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
    val contentValues = ContentValues()
    val dateTaken = System.currentTimeMillis()
    contentValues.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
    contentValues.put(MediaStore.Images.Media.IS_PRIVATE, 1);
    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, DEFAULT_PICTURE_NAME);
    contentValues.put(MediaStore.Images.Media.MIME_TYPE,MIME_TYPE);
    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, DIRECTORY_PICTURES + PICTURE_FILE_DIR_NAME);
    val dateAdded = System.currentTimeMillis();
    contentValues.put(MediaStore.Images.Media.DATE_ADDED, dateAdded);
    val dateModified = System.currentTimeMillis();
    contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, dateModified);
    val insert = context.contentResolver.insert(contentUri, contentValues);
    try {
      val outputStream = insert?.let { context.contentResolver.openOutputStream(it) };
      val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
      bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
      outputStream?.write(33);
      notifyPictureRefresh(arrayOf(getFileDirPath() + DEFAULT_PICTURE_NAME))
    } catch (e: Exception) {
      Logger.e(TAG, "创建默认图异常 ：${e.message}")
    }
  }
}