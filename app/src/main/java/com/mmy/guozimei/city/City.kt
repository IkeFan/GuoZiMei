package com.mmy.guozimei.city

/**
 * @file       City.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/29 0029
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class City {
    var mName:String?= null
    var mPinyin:String? = null
    var mCityId:Int? = 0

    constructor()
    constructor(name:String, pinyin:String):this(name, pinyin,0)
    constructor(name:String, pinyin:String,cityId:Int){
        mName = name
        mPinyin = pinyin
        mCityId = cityId
    }
}