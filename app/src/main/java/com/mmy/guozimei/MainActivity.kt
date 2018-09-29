package com.mmy.guozimei

import android.Manifest
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.MenuItem
import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.AccountInfo
import com.mmy.frame.data.bean.EventBean
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.city.DBHelper
import com.mmy.guozimei.helper.BottomNavigationViewHelper
import com.mmy.guozimei.login.LoginActivity
import com.mmy.guozimei.modules.home.fragments.HomeFragment
import com.mmy.guozimei.modules.hospital.fragments.HospitalFragment
import com.mmy.guozimei.modules.mine.fragments.MineFragment
import com.mmy.guozimei.modules.store.fragments.StoreFragment
import com.squareup.otto.Subscribe
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

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
        var rxPermissions = RxPermissions(this)
        rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { permission ->
                    when {
                        permission.granted -> {
                            // 用户已经同意该权限
                            DBHelper(this).createDataBase()
                        }
                        permission.shouldShowRequestPermissionRationale -> Log.d("ScanPicCameraActivity", permission.name + " is denied. More info should be provided.")
                        else -> {// 用户拒绝了该权限，并且选中『不再询问』
                            "权限不足，请到设置中心开启必要权限".showToast(mFrameApp)
                        }
                    }
                }
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
                        var id =   mFrameApp?.mAccountInfo?.getIdCheckLogin()
                        if(id!=0)
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

    override fun registerBus(): Boolean = true

    @Subscribe
    fun onLoginRequest(accountEvent: AccountInfo.UserEvent){
        openActivity(LoginActivity::class.java)
    }
    @Subscribe
    fun onCityGet(eventBean: EventBean){
        v_location.text = eventBean.meg
    }
}
