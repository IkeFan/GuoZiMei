package com.mmy.guozimei.modules.home.activities

import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import com.mmy.frame.AppComponent
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.HomeBean
import com.mmy.frame.data.bean.IBean
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerActivityComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.common.WebViewActivity
import com.mmy.guozimei.modules.home.adapters.HomeMastersAdapter
import com.mmy.guozimei.modules.home.presenters.HomePresenter
import kotlinx.android.synthetic.main.activity_masters.*

class MastersActivity : BaseActivity<HomePresenter>() {
    val mMastersAdapter = HomeMastersAdapter(R.layout.adapter_master)

    override fun requestSuccess(any: IBean) {
        when(any){
           is HomeBean -> mMastersAdapter.setNewData(any.data)
        }
    }

    override fun setupDagger(appComponent: AppComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .iViewModule(IViewModule(this))
                .build()
                .inject(this)
    }

    override fun initView() {
        setToolbar(getString(R.string.master_appointment), true,textColor =resources.getColor(R.color.white))
        master_list.adapter = mMastersAdapter
        master_list.layoutManager = LinearLayoutManager(this)
    }

    override fun initData() {
        mIPresenter.getMasters(1, 10, false)
    }

    override fun initEvent() {
        toolbar_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0->  mIPresenter.getMasters(1,10,true)
                    1->  mIPresenter.getMasters(1,2,true)
                    2->  mIPresenter.getMasters(1,1,true)
                }
            }

        })

        mMastersAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            var id =   mFrameApp?.mAccountInfo?.getIdCheckLogin()
            if(id!=0){
                var masterId = mMastersAdapter.getItem(position)?.id
                openActivity(WebViewActivity::class.java, "url=" + Config.HOST+"/mobile/great/reserve.html?id>$masterId")
            }
        }

        mMastersAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, baseViewHolder, position ->
            var masterId = mMastersAdapter.getItem(position)?.id
            if(masterId!=null){
                openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/great/detail/id/$masterId.htm")
            }
        }
    }

    override fun getLayoutID(): Any  = R.layout.activity_masters
}
