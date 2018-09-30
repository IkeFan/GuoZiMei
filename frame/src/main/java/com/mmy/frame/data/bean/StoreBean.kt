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
    var data:ArrayList<Product>? = null
    class Product(var id:Int, var goods_name:String? = null, var goods_remark:String, var logo:String,
                  var shop_price:String? = null, var market_price:String, var sales_sum:Int?=0)
}