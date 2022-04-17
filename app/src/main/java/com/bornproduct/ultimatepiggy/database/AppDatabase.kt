package com.bornproduct.ultimatepiggy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bornproduct.ultimatepiggy.database.DataBaseConfig.Companion.databaseName

/**
 * 数据库
 * Created by Born on 2022/04/17
 */
@Database(
  version = DataBaseConfig.databaseVersion,
  entities = DataBaseConfig.tableList
)
abstract class AppDatabase : RoomDatabase() {

  companion object {

    @Volatile
    private var mDatabase: AppDatabase? = null

    @Synchronized
    fun getInstance(context: Context): AppDatabase? {
      if (mDatabase == null) {
        mDatabase = Room.databaseBuilder(context, AppDatabase::class.java,databaseName).build()
      }
      return mDatabase
    }
  }
}