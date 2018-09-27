package com.mmy.guozimei.common

import com.mmy.frame.AppComponent
import com.mmy.frame.base.annotation.ActivityScope
import com.mmy.guozimei.login.LoginActivity
import com.mmy.guozimei.login.RegisterActivity
import com.mmy.guozimei.modules.home.activities.MastersActivity
import dagger.Component

/**
 * @file       ActivityComponent.ktt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/5 0005
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
@ActivityScope
@Component(modules = [IViewModule::class], dependencies = [AppComponent::class])
interface ActivityComponent {
    //inject the activity here which implement IView, just like below
//    fun inject(mainActivity: MainActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(mastersActivity: MastersActivity)
}