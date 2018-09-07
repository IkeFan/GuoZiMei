package com.mmy.guozimei.modules.hospital.fragments

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.mmy.frame.AppComponent
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.HospitalBean
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerFragmentComponent
import com.mmy.guozimei.common.IViewModule
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
class HospitalFragment : BaseFragment<HospitalPresenter>(), BaseQuickAdapter.RequestLoadMoreListener {
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
}