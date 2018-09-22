package com.mmy.guozimei.login

import android.content.Intent
import android.view.View
import com.mmy.frame.AppComponent
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.MainActivity
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerActivityComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.login.presenters.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginPresenter>(), View.OnClickListener{
    override fun requestSuccess(any: IBean) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun setupDagger(appComponent: AppComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .iViewModule(IViewModule(this))
                .build().inject(this)
    }

    override fun initView() {
        setToolbar(getString(R.string.login),true)
    }

    override fun initData() {

    }

    override fun initEvent() {
        arrayOf(tv_register, tv_login).setViewListener(this)
    }

    override fun getLayoutID(): Any = R.layout.activity_login

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_register -> openActivity(RegisterActivity::class.java,"card = " + login_account_input.text)
            R.id.tv_login -> {
                arrayOf(login_account_input, login_pwd_input).forEach {
                    if(it.text.trim().isEmpty()){
                        (it.hint.toString()+"不能为空").showToast(mFrameApp)
                        return
                    }
                }
                mIPresenter.login(login_account_input.text.trim().toString(), login_pwd_input.text.trim().toString())
            }
        }
    }
}
