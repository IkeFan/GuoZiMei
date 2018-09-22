package com.mmy.frame.data.bean

/**
 * @file       RegisterBean.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/22 0022
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class RegisterBean: IBean() {
    var data: Result?=null
    class Result(var member_id: Int)
}