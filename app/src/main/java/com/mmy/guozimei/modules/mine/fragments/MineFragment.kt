package com.mmy.guozimei.modules.mine.fragments

import android.view.View
import com.bumptech.glide.Glide
import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.AccountInfo
import com.mmy.frame.data.bean.IBean
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R
import com.mmy.guozimei.common.WebViewActivity
import com.squareup.otto.Subscribe
import jp.wasabeef.glide.transformations.CropCircleTransformation
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
        if(mFramApp.mAccountInfo.isLogin()){
            userInfoGet(mFramApp.mAccountInfo)
        }
    }

    override fun initEvent() {
        arrayOf(v_settings, mine_history,mine_cure,mine_ticket, mine_report, mine_car,mine_address,mine_card,
                mine_booking,mine_book_buy).setViewListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.v_settings -> {openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/settings")}
            R.id.mine_history-> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/cureHistory")
            R.id.mine_cure->{openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/myConsults")}
            R.id.mine_ticket-> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/myTicket")
            R.id.mine_report -> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/healthReport")
            R.id.mine_car -> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/buyCar")
            R.id.mine_address -> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/myAddress")
            R.id.mine_card -> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/myClassCards")
            R.id.mine_booking -> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/myAppointment")
            R.id.mine_book_buy -> openActivity(WebViewActivity::class.java, "url="+"http://3.soowww.com/index.html#/myClasses")
        }
    }

    @Subscribe
    fun userInfoGet(info: AccountInfo){
        v_name.text = info.nickname
        Glide.with(getAc())
                .load(Config.HOST+info.head_pic)
                .bitmapTransform(CropCircleTransformation(getAc()))
                .placeholder(R.mipmap.ic_default_portrait)
                .error(R.mipmap.ic_default_portrait)
                .into(v_portrait)
    }
    override fun registerBus(): Boolean  = true
}