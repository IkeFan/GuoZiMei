package com.mmy.guozimei.modules.home.fragments

import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import com.mmy.frame.AppComponent
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.HomeBean
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerFragmentComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.modules.home.adapters.HomeMastersAdapter
import com.mmy.guozimei.modules.home.presenters.HomePresenter
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
class HomeFragment: BaseFragment<HomePresenter>(), BaseQuickAdapter.RequestLoadMoreListener {
    override fun onLoadMoreRequested() {

    }

    val mMastersAdapter = HomeMastersAdapter(R.layout.adapter_master)

    override fun requestSuccess(any: IBean) {
        when(any){
            is HomeBean -> mMastersAdapter.setNewData(any.data)
        }
    }

    override fun setupDagger(appComponent: AppComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .iViewModule(IViewModule(this))
                .build().
                inject(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        home_master_list.adapter = mMastersAdapter
        home_master_list.layoutManager = LinearLayoutManager(getAc())
        mMastersAdapter.setEnableLoadMore(true)
    }

    override fun initEvent() {
        toolbar_tab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                mIPresenter.getTestData(true)
            }

        })
    }

    override fun initData() {
        mIPresenter.getTestData(false)
    }
}