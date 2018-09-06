package com.mmy.guozimei

import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.helper.BottomNavigationViewHelper
import com.mmy.guozimei.modules.home.fragments.HomeFragment
import com.mmy.guozimei.modules.hospital.fragments.HospitalFragment
import com.mmy.guozimei.modules.mine.fragments.MineFragment
import com.mmy.guozimei.modules.store.fragments.StoreFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<IPresenter<*>>(){
    val mFragmentTags = arrayOf("home", "store", "hospital", "mine")
    val mFragments = arrayOf(HomeFragment(), StoreFragment(), HospitalFragment(), MineFragment())


    override fun setupDagger(appComponent: AppComponent) {

    }

    override fun initView() {
        BottomNavigationViewHelper.disableShiftMode(bottom_navigation)
        if (supportFragmentManager.findFragmentByTag(mFragmentTags[0])==null){
            supportFragmentManager.beginTransaction()
                    .add(R.id.flg_content, mFragments[0], mFragmentTags[0])
                    .commit()
        }
    }

    override fun initData() {

    }

    override fun initEvent() {
        bottom_navigation.setOnNavigationItemSelectedListener(object: BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        showFragmentByPosition(0)
                        return@onNavigationItemSelected true
                    }
                    R.id.navigation_store -> {
                        showFragmentByPosition(1)
                        return@onNavigationItemSelected true
                    }
                    R.id.navigation_hospital -> {
                        showFragmentByPosition(2)
                        return@onNavigationItemSelected true
                    }
                    R.id.navigation_mine -> {
                        showFragmentByPosition(3)
                        return@onNavigationItemSelected true
                    }
                    else -> return@onNavigationItemSelected false
                }
            }

        })
    }

    override fun getLayoutID(): Any  = R.layout.activity_main

    override fun requestSuccess(any: IBean) {

    }

    private fun showFragmentByPosition(order: Int) {
        val showFragment = supportFragmentManager.findFragmentByTag(mFragmentTags[order])
        val transaction = supportFragmentManager.beginTransaction()
        if (showFragment == null) {
            transaction.add(R.id.flg_content, mFragments[order], mFragmentTags[order])
        } else {
            transaction.show(showFragment)
        }
        mFragmentTags.forEach {
            if (!it.equals(mFragmentTags[order])) {
                val fragment = supportFragmentManager.findFragmentByTag(it)
                if (fragment != null)
                    transaction.hide(fragment)
            }
        }
        transaction.commit()
    }

}
