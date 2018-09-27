package com.mmy.frame.data.bean

/**
 * @file       BannerBean.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/25 0025
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class BannerBean :IBean(){
    var data:ArrayList<Banner>?=null
    class Banner(var title:String, var content:String, var url:String)
}