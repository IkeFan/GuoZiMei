package com.mmy.guozimei.modules.store.presenters

import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.mvp.IView
import com.mmy.frame.data.bean.IBean
import javax.inject.Inject

/**
 * @file       StorePresenter.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/7 0007
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class StorePresenter @Inject constructor() : IPresenter<IView>() {
    fun getProduct(cid: Int, page:Int, limit:Int?=null, showLoading:Boolean) {
        mM.request {
            if(showLoading){
                mV.showLoading()
            }
            call = mApi.getGoodsList(page, cid, limit)
            _success = {
                if(showLoading){
                    mV.hidLoading()
                }
                if(it is IBean){
                    if(it.code == 1){
                        mV.requestSuccess(it)
                    }else{
                        it.msg.showToast(mFrameApp)
                    }
                }else{
                    "数据异常".showToast(mFrameApp)
                }

            }
            _fail = {
                if(showLoading){
                    mV.hidLoading()
                }
                it.message?.showToast(mFrameApp)
            }
        }
    }

    fun getTestProduct(type: Int, showLoading: Boolean) {
//        if (showLoading) {
//            mV.showLoading()
//        }
//        mHandler.postDelayed({
//            var data = StoreBean()
//            data.code = 1
//            data.data = arrayListOf(
//                    StoreBean.Product(1,"限量预售300盒", "武当山初生蛋",
//                            R.mipmap.store_product1, "100.00", "180.00", 2457),
//
//                    StoreBean.Product(2,"限量预售100盒", "武当山野生菌",
//                            R.mipmap.store_product3, "100.00", "380.00", 5014),
//
//                    StoreBean.Product(3,"限量预售200盒", "娄底初生蛋",
//                            R.mipmap.store_product2, "100.00", "180.00", 1450),
//
//
//                    StoreBean.Product(4,"限量预售200盒", "武当山野生干菌",
//                            R.mipmap.store_product4, "100.00", "980.00", 1450),
//
//                    StoreBean.Product(5,"限量预售200盒", "四川野生菌",
//                            R.mipmap.store_product3, "100.00", "280.00", 135)
//            )
//            if (showLoading) {
//                mV.hidLoading()
//            }
//            mV.requestSuccess(data)
//        }, 500)
    }
}