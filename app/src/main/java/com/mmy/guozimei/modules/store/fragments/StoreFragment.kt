package com.mmy.guozimei.modules.store.fragments

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mmy.frame.AppComponent
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.adapter.BaseViewHolder
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.IBean
import com.mmy.frame.data.bean.StoreBean
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerFragmentComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.common.WebViewActivity
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
class StoreFragment : BaseFragment<StorePresenter>() ,BaseQuickAdapter.RequestLoadMoreListener{
    override fun onLoadMoreRequested() {
//        mIPresenter.getTestProduct(1, true)
//        mAdapter.setEnableLoadMore(false)
    }

    val mAdapter = StoreProductAdapter(R.layout.adapter_product)

    override fun requestSuccess(any: IBean) {
       when(any){
           is StoreBean -> {
               mAdapter.setNewData(any.data)
//               if(mAdapter.data.isEmpty()) {
//                   mAdapter.setNewData(any.data)
//               }
//               else if(any.data!=null){
//                   mAdapter.addData(any.data!!)
//                   mAdapter.loadMoreComplete()
//               }
           }
           else ->{

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

    override fun getLayoutId(): Int = R.layout.fragment_store

    override fun initView() {
        store_list.adapter = mAdapter
        store_list.layoutManager = LinearLayoutManager(getAc())
        mAdapter.setOnLoadMoreListener(this, store_list)
    }

    override fun initData() {
        mIPresenter.getTestProduct(0, false)
    }

    override fun initEvent() {
        radio_group1.setOnCheckedChangeListener({_,id->
            when(id){
                R.id.store_all_cb ->{ mIPresenter.getTestProduct(0, true)}
                R.id.store_wudangshan_cb -> { mIPresenter.getTestProduct(0, true)}
                R.id.store_sichuan_cb -> { mIPresenter.getTestProduct(0, true)}
                R.id.store_other_cb -> { mIPresenter.getTestProduct(0, true)}
            }
        })

        radio_group2.setOnCheckedChangeListener({ _,id ->
            when(id){
                R.id.store_compex_cb -> { mIPresenter.getTestProduct(0, true)}
                R.id.store_sale_cb-> { mIPresenter.getTestProduct(0, true) }
                R.id.store_screen -> { mIPresenter.getTestProduct(0, true) }
            }
        })

        mAdapter.onItemClickListener = object :BaseQuickAdapter.OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, baseViewHolder: BaseViewHolder?, position: Int) {
                openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/details.html")
            }

        }
    }
}