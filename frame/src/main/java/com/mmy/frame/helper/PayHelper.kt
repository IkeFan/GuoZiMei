package com.mmy.frame.helper

import android.app.Activity
import android.content.Context
import android.util.Log
import com.mmy.frame.utils.Config
import com.mmy.guozimei.PayBean
import com.mmy.guozimei.PayIBean
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.logging.Handler
import javax.inject.Inject

/**
 * @file       PayHelper.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/27 0027
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */

class PayHelper @Inject constructor(){
    val WEIXIN = 0
    val ALY = 1
    val BANK_CAR = 2
    val BALANCE = 3
    private var msgApi: com.tencent.mm.opensdk.openapi.IWXAPI? = null
    var order_sn:String? = null

    @Inject lateinit var mHandler: Handler

    fun init(context: Context){
        msgApi = com.tencent.mm.opensdk.openapi.WXAPIFactory.createWXAPI(context, null)
        msgApi?.registerApp(Config.WEIXIN_APPKEY)
    }

    @Deprecated("pay")
    fun pay(type: Int, data: PayIBean) {
        when (type) {
            WEIXIN -> payWeixin(data)
        }
    }

    fun pay(activity: Activity, type: Int, data: PayIBean) {
        when (type) {
            WEIXIN -> payWeixin(data)
            ALY -> aliPay(activity, data)
        }
    }

    private fun aliPay(activity: Activity, data: PayIBean) {
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .subscribe({ payIBean ->
                    val payTask = com.alipay.sdk.app.PayTask(activity)
                    val resData = payTask.payV2(payIBean.order_sn, true)
                    if (onPaySuccess != null) {
                        onPaySuccess(resData)
                    }
                    for (s in resData.keys) {
                        val s1 = resData[s]
                        Log.d("PayHelper", s + ":" + s1)
                    }
                })
    }

    private fun payWeixin(data: PayIBean) {
        order_sn = data.order_sn
        val payBean = data as PayBean
        val request = com.tencent.mm.opensdk.modelpay.PayReq()
        request.appId = Config.WEIXIN_APPKEY
        request.partnerId = payBean.mch_id
        request.prepayId = payBean.prepay_id
        request.packageValue = "Sign=WXPay"
        request.nonceStr = payBean.nonce_str
        request.timeStamp = payBean.timestamp
        request.sign = payBean.sign
        msgApi?.sendReq(request)
    }

    var onPaySuccess:(Any)->Unit = {any ->

    }
}