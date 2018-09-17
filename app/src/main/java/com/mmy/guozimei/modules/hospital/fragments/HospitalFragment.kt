package com.mmy.guozimei.modules.hospital.fragments

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mmy.frame.AppComponent
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.HospitalBean
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerFragmentComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.common.WebViewActivity
import com.mmy.guozimei.modules.hospital.adapters.HospitalAdapter
import com.mmy.guozimei.modules.hospital.presenters.HospitalPresenter
import kotlinx.android.synthetic.main.fragment_hospital.*

/**
 * @file       HomeFragment.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/5 0005
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class HospitalFragment : BaseFragment<HospitalPresenter>(), BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    override fun onLoadMoreRequested() {

    }

    val mAdapter = HospitalAdapter(R.layout.adapter_hospital)

    override fun requestSuccess(any: IBean) {
        when(any){
            is HospitalBean -> {
                mAdapter.setNewData(any.data)
                mAdapter.loadMoreEnd()
            }
        }
    }

    override fun setupDagger(appComponent: AppComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .iViewModule(IViewModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_hospital

    override fun initView() {
        hospital_list.adapter = mAdapter
        hospital_list.layoutManager = LinearLayoutManager(getAc())
        hospital_list.addItemDecoration(DividerItemDecoration(getAc(),LinearLayoutManager.VERTICAL))
        mAdapter.setOnLoadMoreListener(this, hospital_list)
    }

    override fun initData() {
        mIPresenter.getTestHospitals(false)
    }

    override fun initEvent() {
        arrayOf(v_medicate_diet, v_cure_book, v_acupuncture, v_consult,
                special_img1, special_img2, special_img3, special_more,
                cure_img1, cure_img2, cure_img3, cure_more, hospital_more).setViewListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.v_consult -> openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/category.html")
            R.id.v_cure_book->openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/doctor.html")
            R.id.v_medicate_diet -> openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/medicined.html")
            R.id.v_acupuncture -> openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/medical.html")
        }
    }
}