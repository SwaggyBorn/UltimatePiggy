package com.bornproduct.ultimatepiggy.database

import android.util.ArraySet
import androidx.room.Entity
import com.bornproduct.ultimatepiggy.basic.BaseActivity
import kotlin.reflect.KClass


/**
 * 数据库信息
 * Created by Born on 2022/04/17
 */
class DataBaseConfig {
	companion object{
		const val databaseVersion = 1
		const val databaseName = "ultimate_piggy.db"
		val tableList : Array<KClass<*>> by lazy {
			val list = Array<KClass<*>>()
				list.add(BaseActivity::class)
				list
		}
	}
}