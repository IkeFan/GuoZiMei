package com.mmy.guozimei.modules.store.fragments

import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import com.mmy.frame.AppComponent
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.IBean
import com.mmy.frame.data.bean.StoreBean
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerFragmentComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.modules.store.adapters.StoreProductAdapter
import com.mmy.guozimei.modules.store.presenters.StorePresenter
import kotlinx.android.synthetic.main.fragment_store.*

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
class StoreFragment : BaseFragment<StorePresenter>() {
    val mAdapter = StoreProductAdapter(R.layout.adapter_product)

    override fun requestSuccess(any: IBean) {
       when(any){
           is StoreBean -> mAdapter.setNewData(any.data)
       }
    }

    override fun setupDagger(appComponent: AppComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .iViewModule(IViewModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_store

    override fun initView() {
        store_list.adapter = mAdapter
        store_list.layoutManager = LinearLayoutManager(getAc())
    }

    override fun initData() {
        mIPresenter.getTestProduct(0, false)
    }

    override fun initEvent() {
        tab_bar_1.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                mIPresenter.getProduct(tab?.position!!)
            }

        })

        tab_bar_1.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                mIPresenter.getProduct(tab?.position!!)
            }

        })
    }
}