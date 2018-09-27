package com.mmy.guozimei.modules.hospital.presenters

import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.mvp.IView
import com.mmy.frame.data.bean.HospitalBean
import com.mmy.guozimei.R
import javax.inject.Inject

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HospitalPresenter @Inject constructor() : IPresenter<IView>() {
    fun getHospitals() {

    }

    fun getTestHospitals(showLoading: Boolean) {
        if (showLoading) {
            mV.showLoading()
        }
        mHandler.postDelayed({
            var hospitalBean = HospitalBean()
            hospitalBean.data = arrayListOf(
                    HospitalBean.Hospital("南方医科大学顺德医院", 1224,
                            R.mipmap.pic_hospital1, 0, 1033, "全国百强 \"改善服务创新医院\"", "佛山市顺德区伦敦街道办事处荔村村委会甲子1号"),
                    HospitalBean.Hospital("南方医科大学广州医院", 2570,
                            R.mipmap.pic_hospital1, 0, 2500, "全国百强 \"改善服务创新医院\"", "广州市越秀区学府路288号"),
                    HospitalBean.Hospital("深圳南山医院", 2529,
                            R.mipmap.pic_hospital2, 0, 2356, "全国百强 \"创新智慧医院\"", "深圳市南山区桃园路177号"),
                    HospitalBean.Hospital("深圳西丽医院", 1224,
                            R.mipmap.pic_hospital2, 0, 1033, "全国百强 \"改善服务创新医院\"", "深圳南山区西丽路南129号"),
                    HospitalBean.Hospital("哈尔滨军工医院深圳分院", 1224,
                            R.mipmap.pic_hospital1, 0, 1033, "全国百强 \"新型智慧科技医院\"", "深圳市南山区高新南三道3081号")
            )
            if (showLoading) {
                mV.hidLoading()
            }
            mV.requestSuccess(hospitalBean)
        }, 500)
    }
}