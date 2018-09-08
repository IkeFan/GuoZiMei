package com.mmy.guozimei.modules.home.fragments

import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
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
class HomeFragment: BaseFragment<HomePresenter>(), BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
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

        arrayOf(v_location, v_scan, v_banner, v_knowledge, v_solution, v_solution_more, v_class,
                card_knowledge_one, card_knowledge_two, card_knowledge_three, card_knowledge_four,
                iv_solution, iv_health_1, iv_health_2,
                v_test, v_answer, v_needed, v_activities, v_book, v_knowledge_more, v_book_more).setViewListener(this)
    }

    override fun initData() {
        mIPresenter.getTestData(false)
    }

    override fun onClick(v: View?) {

    }
}