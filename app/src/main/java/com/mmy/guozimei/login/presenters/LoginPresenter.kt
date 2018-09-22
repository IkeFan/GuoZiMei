package com.mmy.guozimei.login.presenters

import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.mvp.IView
import com.mmy.frame.data.bean.IBean
import javax.inject.Inject

/**
 * @file       LoginPresenter.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/22 0022
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class LoginPresenter @Inject constructor() : IPresenter<IView>() {
    fun login(card: String, password: String) {
        mV.showLoading()
        mM.request {
            call = mApi.login(card, password)
            _success = {
                mV.hidLoading()
                if (it is IBean) {
                    it.msg?.showToast(mFrameApp)
                    if (it.code == 1) {
                        mV.requestSuccess(it)
                    }
                } else {
                    "请求失败".showToast(mFrameApp)
                }
            }
            _fail = {
                mV.hidLoading()
                it.message?.showToast(mFrameApp)
            }
        }
    }
}