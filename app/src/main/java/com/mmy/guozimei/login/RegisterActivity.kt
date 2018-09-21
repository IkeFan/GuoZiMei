package com.mmy.guozimei.login

import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R

class RegisterActivity :BaseActivity<IPresenter<*>>() {

    override fun requestSuccess(any: IBean) {

    }

    override fun setupDagger(appComponent: AppComponent) {

    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun getLayoutID(): Any = R.layout.activity_register
}
