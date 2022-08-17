package com.bornproduct.ultimatepiggy.basic.database

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import java.lang.reflect.ParameterizedType


/**
 * 数据库基本操作类
 * Created by Born on 2022/04/17
 */
abstract class BaseDao<T> {

  /**
   * 添加单个对象
   * @param obj 对象
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insert(obj: T): Long


  /**
   * 添加数组对象数据
	 * @param objArray 数组对象
	 */
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(vararg objArray: T): LongArray?


	/**
	 * 添加表对象集合
	 * @param tList 表对象集合
	 */
	@Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insert(tList: List<T>): List<Long>


  /**
   * 根据表对象中的主键（主键是自动增长的，无需手动赋值）
   * @param obj 表对象
   */
  @Delete
  abstract fun delete(obj: T)


  /**
   * 根据对象中的主键更新（主键是自动增长的，无需手动赋值）
   * @param obj 更新表对象
   */
  @Update
  abstract fun update(vararg obj: T): Int


  /**
   * 删除全部
   */
  fun deleteAll(): Int {
    val query = SimpleSQLiteQuery(
      "delete from $tableName"
    )
    return doDeleteAll(query)
  }


  /**
   * 查询全部
   */
  fun findAll(): List<T>? {
    val query = SimpleSQLiteQuery(
      "select * from $tableName"
    )
    return doFindAll(query)
  }


  /**
   * 根据id主键查找
   * @param id 主键
   */
  fun find(id: Long): T? {
    val query = SimpleSQLiteQuery(
      "select * from $tableName where id = ?", arrayOf<Any>(id)
    )
    return doFind(query)
  }


  // 获取表名
  private val tableName: String
    get() {
      val clazz = (javaClass.superclass.genericSuperclass as ParameterizedType)
        .actualTypeArguments[0] as Class<*>
      return clazz.simpleName
    }

  @RawQuery
  protected abstract fun doFindAll(query: SupportSQLiteQuery?): List<T>?

  @RawQuery
  protected abstract fun doFind(query: SupportSQLiteQuery?): T

  @RawQuery
  protected abstract fun doDeleteAll(query: SupportSQLiteQuery?): Int

  @RawQuery
  protected abstract fun doDeleteByParams(query: SupportSQLiteQuery?): Int

  @RawQuery
  protected abstract fun doQueryByLimit(query: SupportSQLiteQuery?): List<T>?

  @RawQuery
  protected abstract fun doQueryByOrder(query: SupportSQLiteQuery?): List<T>?
}