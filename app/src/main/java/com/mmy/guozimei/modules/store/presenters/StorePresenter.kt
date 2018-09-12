package com.mmy.guozimei.modules.store.presenters

import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.mvp.IView
import com.mmy.frame.data.bean.StoreBean
import com.mmy.guozimei.R
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
    fun getProduct(type: Int) {

    }

    fun getTestProduct(type: Int, showLoading: Boolean) {
        if (showLoading) {
            mV.showLoading()
        }
        mHandler.postDelayed({
            var data = StoreBean()
            data.status == 1
            data.data = arrayListOf(
                    StoreBean.Product("限量预售300盒", "武当山初生蛋",
                            R.mipmap.store_product1, "武当山", "180.00", 2457),

                    StoreBean.Product("限量预售100盒", "武当山野生菌",
                            R.mipmap.store_product3, "武当山", "380.00", 5014),

                    StoreBean.Product("限量预售200盒", "娄底初生蛋",
                            R.mipmap.store_product2, "四川娄底", "180.00", 1450),


                    StoreBean.Product("限量预售200盒", "武当山野生干菌",
                            R.mipmap.store_product4, "武当山", "980.00", 1450),

                    StoreBean.Product("限量预售200盒", "四川野生菌",
                            R.mipmap.store_product3, "四川娄底", "280.00", 135)
            )
            if (showLoading) {
                mV.hidLoading()
            }
            mV.requestSuccess(data)
        }, 500)
    }
}