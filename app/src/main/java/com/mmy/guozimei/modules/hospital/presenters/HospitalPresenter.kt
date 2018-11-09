package com.mmy.guozimei.modules.hospital.presenters

import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.mvp.IView
import com.mmy.frame.data.bean.HospitalBean
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import javax.inject.Inject

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HospitalPresenter @Inject constructor() : IPresenter<IView>() {

    fun getHospital(cid:Int, type: Int?=null, page:Int?=null,limit: Int?=null, order:Int?=null){
        mM.request {
            call = mApi.getHospital(cid = cid, limit = limit)
            _success = {
                if(it is IBean){
                    mV.requestSuccess(it)
                }
            }
            _fail = {
                it.message?.showToast(mFrameApp)
            }
        }
    }
}