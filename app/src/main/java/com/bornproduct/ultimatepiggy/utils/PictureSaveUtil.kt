package com.bornproduct.ultimatepiggy.utils

import android.content.ContentUris
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




class PictureSaveUtil(private val context: Context) {

  companion object {
    private const val TAG = "PictureSaveUtil"
    const val PICTURE_FILE_DIR_NAME = "SexyPiggy"
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
    contentValues.put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE);
    contentValues.put(
      MediaStore.Images.Media.RELATIVE_PATH,
      "$DIRECTORY_PICTURES/$PICTURE_FILE_DIR_NAME"
    );
    contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
    contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis());
    val insert = context.contentResolver.insert(contentUri, contentValues);
    try {
      val outputStream = insert?.let { context.contentResolver.openOutputStream(it) };
      val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
      bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
      notifyPictureRefresh(arrayOf(getFileDirPath() + DEFAULT_PICTURE_NAME))
    } catch (e: Exception) {
      Logger.e(TAG, "创建默认图异常 ：${e.message}")
    }
  }

  /**
   * 搜索文件夹下的所有图片路径
   */
  fun getAllImgPath() {

    // 先拿到图片数据表的uri
    val tableUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    // 需要获取数据表中的哪几列信息
    val projection = arrayOf(
      MediaStore.Video.Media._ID,
      MediaStore.Video.Media.DATA,
      MediaStore.Video.Media.DISPLAY_NAME,
      MediaStore.Video.Media.MIME_TYPE,
      MediaStore.Video.Media.BUCKET_DISPLAY_NAME
    )
    val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "= ?";
    // 条件参数 ，因为是查询全部图片，传null
    val args = arrayOf(PICTURE_FILE_DIR_NAME)
    // 排序：按id倒叙
    val order = MediaStore.Files.FileColumns._ID + " DESC"
    // 开始查询
    val cursor = context.contentResolver.query(tableUri, projection, selection, args, order);
    if (cursor != null) {
      try {

        // 获取id字段index
        val idIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        // 获取data字段index
        val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        // 获取文件名称字段index
        val displayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)

        //循环遍历
        while (cursor.moveToNext()) {
          val id = cursor.getLong(idIndex);
          // 获取到每张图片的uri
          val imageUri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
          // 获取到每张图片的绝对路径
          val path = cursor.getString(dataIndex);
          // 获取到每张图片的名称
          val displayName = cursor.getString(displayNameIndex)

          Logger.d(TAG, "搜索到图片 ：$displayName")
        }
      } catch (e: Exception) {
        Logger.e(TAG, "搜索图片异常 ：${e.message}")
      } finally {
        cursor.close();
      }
    }
  }
}