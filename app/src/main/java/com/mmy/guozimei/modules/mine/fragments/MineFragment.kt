package com.mmy.guozimei.modules.mine.fragments

import android.view.View
import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import com.mmy.guozimei.common.WebViewActivity
import kotlinx.android.synthetic.main.fragment_mine.*

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
class MineFragment : BaseFragment<IPresenter<*>>(), View.OnClickListener {
    override fun requestSuccess(any: IBean) {

    }

    override fun setupDagger(appComponent: AppComponent) {

    }

    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initEvent() {
        arrayOf(v_settings, mine_history,mine_cure,mine_ticket).setViewListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.v_settings -> {openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/settings")}
            R.id.mine_history-> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/cureHistory")
            R.id.mine_cure->{openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/myConsults")}
            R.id.mine_ticket-> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/myTicket")
        }
    }
}