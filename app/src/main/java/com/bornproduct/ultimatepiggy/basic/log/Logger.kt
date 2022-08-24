package com.bornproduct.ultimatepiggy.basic.log


import android.app.Application
import android.os.FileUtils
import android.util.Log
import com.bornproduct.ultimatepiggy.basic.MainApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors



object Logger {

  private val TAG by lazy { Application.getProcessName() }
  private const val CHUNK_SIZE = 106 //设置字节数
  private const val TOP_BORDER =
    "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════"
  private const val LEFT_BORDER = "║ "
  private const val BOTTOM_BORDER =
    "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════"
  private var debug: Boolean = true//是否打印log
  private var savesd: Boolean = false//是否存log到sd卡
  private var logDir = ""//设置文件存储目录
  private var logSize = 2 * 1024 * 1024L//设置log文件大小 k
  private val threadPool: ExecutorService = Executors.newFixedThreadPool(1)

  init {
    initLogFile()
  }

  fun v(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.VERBOSE)
  fun d(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.DEBUG)
  fun i(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.INFO)
  fun w(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.WARN)
  fun e(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.ERROR)

  private fun targetStackTraceMSg(): String {
    val targetStackTraceElement = getTargetStackTraceElement()
    return if (targetStackTraceElement != null) {
      "print at ${targetStackTraceElement.className}" +
          ".${targetStackTraceElement.methodName}" +
          "(${targetStackTraceElement.fileName}" +
          ":${targetStackTraceElement.lineNumber})"
    } else {
      ""
    }
  }

  private fun getTargetStackTraceElement(): StackTraceElement? {
    var targetStackTrace: StackTraceElement? = null
    var shouldTrace = false
    val stackTrace = Thread.currentThread().stackTrace
    for (stackTraceElement in stackTrace) {
      val isLogMethod = stackTraceElement.className == Logger::class.java.name
      if (shouldTrace && !isLogMethod) {
        targetStackTrace = stackTraceElement
        break
      }
      shouldTrace = isLogMethod
    }
    return targetStackTrace
  }


  private fun initLogFile() {
//    logDir = "${FileUtils.getRootDir()}/hotapk.cn"
//    FileUtils.mkDir(logDir)
  }

  private fun Boolean.debugLog(tag: String, msg: String, type: Int) {
    if (!this) {
      return
    }
    val newMsg = if (type == Log.ERROR) msgFormat(msg) else msg

    savesd.saveToSd(
      "${
        SimpleDateFormat(
          "yyyy-MM-dd HH:mm:ss",
          Locale.US
        ).format(Date())
      }\n${targetStackTraceMSg()}", msg
    )
    when (type) {
      Log.VERBOSE -> Log.v(tag, newMsg)
      Log.DEBUG -> Log.d(tag, newMsg)
      Log.INFO -> Log.i(tag, newMsg)
      Log.WARN -> Log.w(tag, newMsg)
      Log.ERROR -> Log.e(tag, newMsg)
    }

  }

  private fun msgFormat(msg: String): String {
    val bytes: ByteArray = msg.toByteArray()
    val length = bytes.size
    var newMsg = "\t\n$TOP_BORDER"
    if (length > CHUNK_SIZE) {
      var i = 0
      while (i < length) {
        val count = Math.min(length - i, CHUNK_SIZE)
        val tempStr = String(bytes, i, count)
        newMsg += "\n$LEFT_BORDER\t$tempStr"
        i += CHUNK_SIZE
      }
    } else {
      newMsg += "\n$LEFT_BORDER\t$msg"
    }
    newMsg += "\n$LEFT_BORDER\n$LEFT_BORDER\t${targetStackTraceMSg()}\n$BOTTOM_BORDER\n\t\n\t"
    return newMsg

  }

  private fun Boolean.saveToSd(tag: String, msg: String) {
//    if (!this) {
//      return
//    }
//    threadPool.submit {
//      val data = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
//      val files = FileUtils.sortByTime(File(logDir))?.filter { it -> it.name.contains(data) }
//      val filepath: String
//      if (files != null && files.isNotEmpty()) {
//        val length: Long = FileUtils.getLeng(files[0])
//        if (length > logSize) {
//          val id = files[0].name.replace("${data}_", "").replace(".log", "").toInt() + 1
//          filepath = "$logDir/${data}_$id.log"
//          FileUtils.creatFile(filepath)
//        } else {
//          filepath = files[0].absolutePath
//        }
//      } else {
//        filepath = "$logDir/${data}_1.log"
//        FileUtils.creatFile(filepath)
//      }
//      FileUtils.appendText(File(filepath), "\r\n$tag\n$msg")
//    }

  }


  /**
   * 是否打印log输出
   * @param debug
   */
  fun debug(debug: Boolean): Logger {
    Logger.debug = debug
    return this
  }

  /**
   * 是否保存到sd卡
   * @param isSave
   */
  fun saveSd(isSave: Boolean): Logger {
    Logger.savesd = isSave
    return this
  }

  /**
   * 设置每个log的文件大小
   * @param logSize 文件大小 byte
   */
  fun logSize(logSize: Long): Logger {
    Logger.logSize = logSize
    return this

  }

  /**
   * 设置log文件目录
   * @param logDir 文件目录
   */
  fun logDir(logDir: String): Logger {
    Logger.logDir = logDir
    return this
  }


}
