package com.mmy.guozimei.modules.home.fragments

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.mmy.frame.AppComponent
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.*
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R
import com.mmy.guozimei.ScanPicCameraActivity
import com.mmy.guozimei.city.CitySearchActivity
import com.mmy.guozimei.common.BannerAdapter
import com.mmy.guozimei.common.DaggerFragmentComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.common.WebViewActivity
import com.mmy.guozimei.modules.home.activities.MastersActivity
import com.mmy.guozimei.modules.home.adapters.HomeMastersAdapter
import com.mmy.guozimei.modules.home.presenters.HomePresenter
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_home.*
import me.crosswall.lib.coverflow.CoverFlow

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
    var dragging = false
    override fun onLoadMoreRequested() {

    }

    val mMastersAdapter = HomeMastersAdapter(R.layout.adapter_master)
    var mBannerAdapter:BannerAdapter? = null

    override fun requestSuccess(any: IBean) {
        if(any.code !=1){
            if(mBannerAdapter==null){
                mIPresenter.getBanner()
            }
            return@requestSuccess
        }
        when(any){
            is HomeBean ->{
                mMastersAdapter.setNewData(any.data)
                mIPresenter.getArticle(17, 1, 1, 4, false)
            }
            is BannerBean -> {
                initBanner(any.data!!)
                if(mMastersAdapter.data.size == 0){
                    mIPresenter.getMasters(1,10,false)
                }
            }
            is ArticleBean ->{
                var i=0
                any.data?.forEach{
                    when(i){
                        0->{
                            Glide.with(this)
                                    .load(Config.HOST + it.litpic)
                                    .error(R.mipmap.knowledge_one)
                                    .placeholder(R.mipmap.knowledge_one)
                                    .into(knowledge_one)
                            knowledge_one_text.text = it.title
                            knowledge_one_description.text = it.description
                        }
                        1->{
                            Glide.with(this)
                                    .load(Config.HOST + it.litpic)
                                    .error(R.mipmap.knowledge_two)
                                    .placeholder(R.mipmap.knowledge_two)
                                    .into(knowledge_two)
                            knowledge_two_text.text = it.title
                            knowledge_two_description.text = it.description
                        }
                        2->{
                            Glide.with(this)
                                    .load(Config.HOST + it.litpic)
                                    .error(R.mipmap.knowledge_three)
                                    .placeholder(R.mipmap.knowledge_three)
                                    .into(knowledge_two)
                            knowledge_three_text.text = it.title
                            knowledge_three_description.text = it.description
                        }
                        3->{
                            Glide.with(this)
                                    .load(Config.HOST + it.litpic)
                                    .error(R.mipmap.knowledge_four)
                                    .placeholder(R.mipmap.knowledge_four)
                                    .into(knowledge_four)
                            knowledge_four_text.text = it.title
                            knowledge_four_description.text = it.description
                        }
                    }
                    i++
                }
                mIPresenter.getCateArticle(17, 1, 10)
            }
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
    }

    private fun initBanner( data: ArrayList<BannerBean.Banner>){
        mBannerAdapter = BannerAdapter(getAc(), data,overlap_pager)
        overlap_pager.adapter = mBannerAdapter
        overlap_pager.clipChildren = false

        overlap_pager.offscreenPageLimit = 3

        overlap_pager.currentItem = 20
        CoverFlow.Builder()
                .with(overlap_pager)
                .scale(0.15f)
                .pagerMargin(resources.getDimensionPixelSize(R.dimen.pager_margin).toFloat())
                .spaceSize(0f)
                .build()

        startLoop()
    }

    override fun initEvent() {
        toolbar_tab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0->  mIPresenter.getMasters(1,10,true)
                    1->  mIPresenter.getMasters(1,2,true)
                    2->  mIPresenter.getMasters(1,1,true)
                }
            }

        })

        overlap_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                when(state){
                    ViewPager.SCROLL_STATE_DRAGGING->{
                        dragging = true
                    }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                dragging = true
            }

            override fun onPageSelected(position: Int) {
                mBannerAdapter?.setCurrentPosition(position)
                mHandler.postDelayed({
                    dragging = false
                }, 600)
            }


        })

        mMastersAdapter.setOnItemClickListener { adapter, view, baseViewHolder, position ->
            openActivity(WebViewActivity::class.java,"url=" + "http://1.soowww.com/introduction.html")
        }

        mMastersAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            var id =   mFramApp?.mAccountInfo?.getIdCheckLogin()
            if(id!=0){
                var masterId = mMastersAdapter.getItem(position)?.id
                openActivity(WebViewActivity::class.java, "url=" + Config.HOST+"/mobile/great/reserve.html?id>$masterId")
            }
        }

        arrayOf(v_location, v_scan, v_knowledge, v_solution, v_solution_more, v_class,
                card_knowledge_one, card_knowledge_two, card_knowledge_three, card_knowledge_four,
                iv_solution, iv_health_1, iv_health_2,
                v_test, v_home_consult, v_master_in, v_activities, v_book, v_knowledge_more, v_book_more).setViewListener(this)
    }

    private fun startLoop(){
        mHandler.postDelayed(looperRun, 3000)
    }

    private val looperRun = Runnable {
        if(overlap_pager==null || dragging ||overlap_pager.isFakeDragging){
            dragging = false
            startLoop()
            return@Runnable
        }
        var currentIndex = overlap_pager.currentItem
        if (++currentIndex == mBannerAdapter?.count) {
            overlap_pager.currentItem = 1
        } else {
            overlap_pager.setCurrentItem(currentIndex, true)
        }
        startLoop()
    }

    override fun initData() {
//        mIPresenter.getTestData(false)
        mIPresenter.getBanner()
//        mIPresenter.getArticle(17, 1, 1, 4, false)
//        mIPresenter.getCateArticle(17, 1, 2)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.v_scan -> {
                openActivity(ScanPicCameraActivity::class.java)
            }
            R.id.v_location -> openActivity(CitySearchActivity::class.java)
            R.id.v_solution -> openActivity(WebViewActivity::class.java,"url="+"http://4.soowww.com/mobile/article/plan.html")
            R.id.v_knowledge -> openActivity(WebViewActivity::class.java,"url="+"http://4.soowww.com/mobile/article/knowledge.html")
            R.id.v_book ->  {
//                openActivity(WebViewActivity::class.java,"url="+"http://1.soowww.com/physician.html")
                openActivity(MastersActivity::class.java)
            }
            R.id.v_class -> openActivity(WebViewActivity::class.java,"url="+"http://4.soowww.com/mobile/article/room.html")
            R.id.v_activities -> openActivity(WebViewActivity::class.java, "url="+"http://4.soowww.com/mobile/article/activity.html")
            R.id.v_test -> openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/testing.html")
            R.id.card_knowledge_one,
            R.id.card_knowledge_two,
            R.id.card_knowledge_three,
            R.id.card_knowledge_four->{openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/smooth.html")}
            R.id.v_home_consult -> openActivity(WebViewActivity::class.java, "url = " + "http://1.soowww.com/consult.html")
            R.id.v_master_in -> openActivity(WebViewActivity::class.java,"url=" + "http://1.soowww.com/master.html")
        }
    }

    @Subscribe
    fun onCityGet(eventBean: EventBean){
        v_location.text = eventBean.meg
    }
    override fun registerBus(): Boolean = true
}