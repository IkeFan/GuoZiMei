package com.mmy.guozimei.common

import com.mmy.frame.AppComponent
import com.mmy.frame.base.annotation.FragmentScope
import com.mmy.guozimei.modules.store.fragments.StoreFragment
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
@FragmentScope
@Component(modules = [IViewModule::class], dependencies = [AppComponent::class])
interface FragmentComponent {
    //inject the Fragment here which implement IView, just like below
//    fun inject(demoFragment: DemoFragment)
    fun inject(storeFragment: StoreFragment)
}