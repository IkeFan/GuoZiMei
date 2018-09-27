package com.mmy.guozimei

/**
 * @file       PayBean.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/27 0027
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class PayBean: PayIBean() {
    var return_code: String? = null
    var return_msg: String? = null
    var appid: String? = null
    var mch_id: String? = null
    var nonce_str: String? = null
    var sign: String? = null
    var result_code: String? = null
    var prepay_id: String? = null
    var trade_type: String? = null
    var timestamp: String? = null
}