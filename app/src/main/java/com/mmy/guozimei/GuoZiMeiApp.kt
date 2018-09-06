package com.mmy.guozimei

import com.mmy.frame.FrameApp

/**
 * @file       GuoZiMeiApp.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/5 0005
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class GuoZiMeiApp : FrameApp() {

    companion object {
        lateinit var instance:GuoZiMeiApp
    }

    override fun initApp() {
        instance = this
    }
}