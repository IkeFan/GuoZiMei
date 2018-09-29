package com.mmy.guozimei.city

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * @file       CityDatabaseHelper.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/29 0029
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class CityDatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "city", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.e("info", "create city table")
        db?.execSQL("CREATE TABLE IF NOT EXISTS recentcity (id integer primary key autoincrement, " + "name varchar(40), date INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}