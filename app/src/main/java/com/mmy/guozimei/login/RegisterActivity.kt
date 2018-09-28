package com.mmy.guozimei.login

import android.content.Intent
import android.view.View
import com.mmy.frame.AppComponent
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.IBean
import com.mmy.frame.data.bean.LoginBean
import com.mmy.guozimei.MainActivity
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerActivityComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.helper.CountDownTask
import com.mmy.guozimei.login.presenters.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity<RegisterPresenter>(), View.OnClickListener, CountDownTask.OnCountDownTaskListener {

    var isForget = false
    var counter: CountDownTask? = null


    override fun progress(progress: Int) {
        get_code.text = progress.toString() + "秒"
    }

    override fun complete() {
        get_code.isEnabled = true
        get_code.isClickable = true
        get_code.setText(R.string.get_verify_code)
    }

    override fun requestSuccess(any: IBean) {
        when (any) {
            is LoginBean -> {
                mFrameApp?.mAccountInfo = any.data
                var intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            is IBean -> {
                if(any.code!=1){
                    counter?.stop()
                    complete()
                }
            }
        }
    }

    override fun setupDagger(appComponent: AppComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .iViewModule(IViewModule(this))
                .build().inject(this)
    }

    override fun initView() {
        if (intent.hasExtra("title")) {
            setToolbar(intent.getStringExtra("title"), true)
            finish_register.setText(R.string.finish)
            invite_input.visibility = View.GONE
            isForget = true
        } else {
            isForget = false
            setToolbar(getString(R.string.register), true)
        }

    }

    override fun initData() {
        if (intent.hasExtra("card")) {
            account_input.setText(intent.getStringExtra("card"))
        }
        counter = CountDownTask(mFrameApp?.handler!!, this)
    }

    override fun getLayoutID(): Any = R.layout.activity_register

    override fun initEvent() {
        arrayOf(get_code, finish_register, void_code).setViewListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.finish_register -> {
                arrayOf(password_input, code_input, account_input).forEach {
                    if (it.text.trim().isEmpty()) {
                        (it.hint.toString() + "不能为空").showToast(mFrameApp)
                        return
                    }
                }
                if (invite_input.text == null || invite_input.text.trim().isEmpty()) {
                    if (isForget) {
                        mIPresenter.forgetPassword(account_input.text.trim().toString(), code_input.text.trim().toString(),
                                password_input.text.trim().toString())
                    } else {
                        mIPresenter.register(account_input.text.trim().toString(), code_input.text.trim().toString(),
                                password_input.text.trim().toString())
                    }

                } else {
                    mIPresenter.register(account_input.text.trim().toString(),
                            code_input.text.trim().toString(), password_input.text.trim().toString(), invite_input.text.trim().toString())
                }

            }
            R.id.get_code -> {
                if (account_input.text.trim().isEmpty()) {
                    ("请输入" + account_input.hint.toString()).showToast(mFrameApp)
                    return
                }
                counter?.start()
                get_code.isEnabled = false
                get_code.isClickable = false
                mIPresenter.getVerifyCode(account_input.text.trim().toString())
            }
        }
    }
}
