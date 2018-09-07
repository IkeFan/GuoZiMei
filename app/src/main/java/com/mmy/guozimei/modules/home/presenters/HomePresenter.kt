package com.mmy.guozimei.modules.home.presenters

import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.mvp.IView
import com.mmy.frame.data.bean.HomeBean
import com.mmy.guozimei.R
import javax.inject.Inject

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HomePresenter @Inject constructor() : IPresenter<IView>() {
    fun getTestData(showLoading: Boolean) {
        if (showLoading) {
            mV.showLoading()
        }
        mHandler.postDelayed({
            var homeBean = HomeBean()
            homeBean.data = listOf(
                    HomeBean.GreatMaster("王明明", 2, "2011年4月被聘任为北大国画教授...",
                            "代表作《草原上的阿里》《阿尔卑斯之冬》", 1, 47, 1, R.mipmap.demo_portrait1),

                    HomeBean.GreatMaster("张飙", 3, "2013年6月被授予为全国10大书法大师之一...",
                            "代表作《大展宏图》《草书行书录》", 1, 76, 2, R.mipmap.demo_portrait2),

                    HomeBean.GreatMaster("杨璐梦", 2, "2012年8月被聘任北京艺术学院教授...",
                            "代表作《青春进行曲》《夜章旋律》", 2, 68, 3, R.mipmap.demo_portrait3),

                    HomeBean.GreatMaster("欧律明", 2, "2011年4月被聘任为国务院参事...",
                            "代表作《秋之枫》《落日余晖》", 1, 39, 1, R.mipmap.ic_default_portrait)
            )

            if (showLoading) {
                mV.hidLoading()
            }
            mV.requestSuccess(homeBean)
        }, 500)
    }
}