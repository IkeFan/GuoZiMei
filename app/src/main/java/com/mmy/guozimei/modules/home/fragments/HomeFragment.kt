package com.mmy.guozimei.modules.home.fragments

import android.support.v7.widget.LinearLayoutManager
import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import com.mmy.guozimei.modules.home.adapters.HomeMastersAdapter
import kotlinx.android.synthetic.main.fragment_home.*

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
class HomeFragment: BaseFragment<IPresenter<*>>() {
    val mMastersAdapter = HomeMastersAdapter(R.layout.adapter_master)

    override fun requestSuccess(any: IBean) {

    }

    override fun setupDagger(appComponent: AppComponent) {

    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        home_master_list.adapter = mMastersAdapter
        home_master_list.layoutManager = LinearLayoutManager(getAc())
        mMastersAdapter.initVirData()
        mMastersAdapter.setEnableLoadMore(true)
    }

    override fun initData() {

    }
}