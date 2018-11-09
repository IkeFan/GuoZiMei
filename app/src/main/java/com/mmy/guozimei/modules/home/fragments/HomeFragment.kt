package com.mmy.guozimei.modules.home.fragments

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
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
import com.mmy.guozimei.modules.home.adapters.HomeCateArticleAdapter
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
    val mCateArticleAdapter = HomeCateArticleAdapter(R.layout.item_home_article_cate)
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

                any.data?.data?.forEach{
                    when(i){
                        0->{
                            Glide.with(this)
                                    .load(Config.HOST + it.litpic)
                                    .error(R.mipmap.ic_launcher)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(knowledge_one)
                            knowledge_one_text.text = it.short_title
//                            knowledge_one_description.text = it.description
                            knowledge_one_text.tag = it.id
                        }
                        1->{
                            Glide.with(this)
                                    .load(Config.HOST + it.litpic)
                                    .error(R.mipmap.ic_launcher)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(knowledge_two)
                            knowledge_two_text.text = it.short_title
//                            knowledge_two_description.text = it.description
                            knowledge_two_text.tag = it.id
                        }
                        2->{
                            Glide.with(this)
                                    .load(Config.HOST + it.litpic)
                                    .error(R.mipmap.ic_launcher)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(knowledge_three)
                            knowledge_three_text.text = it.short_title
//                            knowledge_three_description.text = it.description
                            knowledge_three_text.tag = it.id
                        }
                        3->{
                            Glide.with(this)
                                    .load(Config.HOST + it.litpic)
                                    .error(R.mipmap.ic_launcher)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(knowledge_four)
                            knowledge_four_text.text = it.short_title
//                            knowledge_four_description.text = it.description
                            knowledge_four_text.tag = it.id
                        }
                    }
                    i++
                }
                mIPresenter.getCateArticle(18, 1, 2)
            }
            is ArticleCategoryBean ->{
                mCateArticleAdapter.setNewData(any.data)
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
        article_cate_list.layoutManager = LinearLayoutManager(getAc())
        article_cate_list.adapter = mCateArticleAdapter
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
            var masterId = mMastersAdapter.getItem(position)?.id
            if(masterId!=null){
                openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/great/detail/id/$masterId.htm")
            }
        }

        mMastersAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            var id =   mFramApp?.mAccountInfo?.getIdCheckLogin()
            if(id!=0){
                var masterId = mMastersAdapter.getItem(position)?.id
                openActivity(WebViewActivity::class.java, "url=" + Config.HOST+"/mobile/great/reserve.html?id>$masterId")
            }
        }

        mCateArticleAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{ adapter, view, position ->
            when(view.id){
                R.id.item_more ->{
                    var id =   mCateArticleAdapter.getItem(position)?.id
                    openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/plan/cid/$id.html")
                }
                R.id.left_img ->{
                    var id =   mCateArticleAdapter.getItem(position)?.list?.get(0)?.id
                    Log.e("fuck","fack id"+id +"tile："+ mCateArticleAdapter.getItem(position)?.list?.get(1)?.title)
                    openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/detail/id/$id.html")
                }
                R.id.right_img ->{
                    var id =   mCateArticleAdapter.getItem(position)?.list?.get(1)?.id
                    Log.e("fuck","fack id"+id +"tile："+ mCateArticleAdapter.getItem(position)?.list?.get(1)?.title)
                    openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/detail/id/$id.html")
                 }
            }
         }

        arrayOf(v_location, v_scan, v_knowledge, v_solution, v_solution_more, v_class,
                card_knowledge_one, card_knowledge_two, card_knowledge_three, card_knowledge_four,
                iv_solution,
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
            R.id.v_knowledge_more-> openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/knowledge.html")
            R.id.card_knowledge_one ->{
                var id = knowledge_one_text.tag
                openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/detail/id/$id.html")
            }
            R.id.card_knowledge_two ->{
                var id = knowledge_two_text.tag
                openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/detail/id/$id.html")
            }
            R.id.card_knowledge_three->{
                var id = knowledge_three_text.tag
                openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/detail/id/$id.html")
            }
            R.id.card_knowledge_four->{
                var id = knowledge_four_text.tag
                openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/detail/id/$id.html")
            }
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
