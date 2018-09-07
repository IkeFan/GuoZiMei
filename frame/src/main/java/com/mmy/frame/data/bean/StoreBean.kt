package com.mmy.frame.data.bean

/**
 * @file       StoreBean.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/7 0007
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class StoreBean :IBean(){
    var data:List<Product>? = null
    class Product(var title:String? = null, var name:String, var img:Int?=0,
                  var place:String? = null, var price:String, var payedCount:Int?=0)
}