package com.mmy.guozimei.modules.home.fragments

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mmy.frame.AppComponent
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.HomeBean
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import com.mmy.guozimei.ScanPicCameraActivity
import com.mmy.guozimei.common.BannerAdapter
import com.mmy.guozimei.common.DaggerFragmentComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.common.WebViewActivity
import com.mmy.guozimei.modules.home.adapters.HomeMastersAdapter
import com.mmy.guozimei.modules.home.presenters.HomePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_home.*
import me.crosswall.lib.coverflow.CoverFlow
import java.util.concurrent.TimeUnit

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
class HomeFragment: BaseFragment<HomePresenter>(), BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    var mLooping =true

    override fun onLoadMoreRequested() {

    }

    val mMastersAdapter = HomeMastersAdapter(R.layout.adapter_master)
    var mBannerAdapter:BannerAdapter? = null

    override fun requestSuccess(any: IBean) {
        when(any){
            is HomeBean -> mMastersAdapter.setNewData(any.data)
        }
    }

    override fun setupDagger(appComponent: AppComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .iViewModule(IViewModule(this))
                .build().
                inject(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        home_master_list.adapter = mMastersAdapter
        home_master_list.layoutManager = LinearLayoutManager(getAc())
        mMastersAdapter.setEnableLoadMore(true)

        mBannerAdapter = BannerAdapter(R.layout.adapter_banner, getAc())
        overlap_pager.adapter = mBannerAdapter
        overlap_pager.clipChildren = false
        overlap_pager.offscreenPageLimit = 4

        mBannerAdapter?.setData(intArrayOf(R.mipmap.banner_bg, R.mipmap.banner_front,R.mipmap.banner_bg,R.mipmap.banner_front))
        overlap_pager.setCurrentItem(Integer.MAX_VALUE/2, false)
        mBannerAdapter?.currPosition = overlap_pager.currentItem

        CoverFlow.Builder()
                .with(overlap_pager)
                .scale(0.15f)
                .pagerMargin(resources.getDimensionPixelSize(R.dimen.pager_margin).toFloat())
                .spaceSize(0f)
                .build()
    }

    override fun initEvent() {
        toolbar_tab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                mIPresenter.getTestData(true)
            }

        })

        overlap_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mBannerAdapter?.setCurrentPosition(position)
            }


        })


        Observable.interval(3, 3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { next ->
                    if(overlap_pager==null){
                        return@subscribe
                    }
                    var currentIndex = overlap_pager.currentItem
                    if (++currentIndex == mBannerAdapter?.count) {
                        overlap_pager.currentItem = 1
                    } else {
                        overlap_pager.setCurrentItem(currentIndex, true)
                    }
                }


        arrayOf(v_location, v_scan, v_knowledge, v_solution, v_solution_more, v_class,
                card_knowledge_one, card_knowledge_two, card_knowledge_three, card_knowledge_four,
                iv_solution, iv_health_1, iv_health_2,
                v_test, v_home_consult, v_master_in, v_activities, v_book, v_knowledge_more, v_book_more).setViewListener(this)
    }

    override fun initData() {
        mIPresenter.getTestData(false)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.v_scan -> {
                openActivity(ScanPicCameraActivity::class.java)
            }
            R.id.v_solution -> openActivity(WebViewActivity::class.java,"url="+"http://1.soowww.com/program.html")
            R.id.v_knowledge -> openActivity(WebViewActivity::class.java,"url="+"http://1.soowww.com/health.html")
            R.id.v_book -> openActivity(WebViewActivity::class.java,"url="+"http://1.soowww.com/physician.html")
            R.id.v_class -> openActivity(WebViewActivity::class.java,"url="+"http://1.soowww.com/course.html")
            R.id.v_activities -> openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/activity.html")
            R.id.card_knowledge_one,
            R.id.card_knowledge_two,
            R.id.card_knowledge_three,
            R.id.card_knowledge_four->{openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/smooth.html")}
            R.id.v_home_consult -> openActivity(WebViewActivity::class.java, "url = " + "http://1.soowww.com/consult.html")
            R.id.v_master_in -> openActivity(WebViewActivity::class.java,"url=" + "http://1.soowww.com/master.html")
        }
    }
}