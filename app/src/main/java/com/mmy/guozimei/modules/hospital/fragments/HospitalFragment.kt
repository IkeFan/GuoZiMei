package com.mmy.guozimei.modules.hospital.fragments

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.mmy.frame.AppComponent
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.base.view.BaseFragment
import com.mmy.frame.data.bean.ArticleBean
import com.mmy.frame.data.bean.ArticleCategoryBean
import com.mmy.frame.data.bean.HospitalBean
import com.mmy.frame.data.bean.IBean
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R
import com.mmy.guozimei.common.DaggerFragmentComponent
import com.mmy.guozimei.common.IViewModule
import com.mmy.guozimei.common.WebViewActivity
import com.mmy.guozimei.modules.hospital.adapters.HospitalAdapter
import com.mmy.guozimei.modules.hospital.presenters.HospitalPresenter
import kotlinx.android.synthetic.main.fragment_hospital.*

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
class HospitalFragment : BaseFragment<HospitalPresenter>(), BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    override fun onLoadMoreRequested() {

    }

    val mAdapter = HospitalAdapter(R.layout.adapter_hospital)

    override fun requestSuccess(any: IBean) {
        when(any){
            is HospitalBean -> {
                when(any?.data?.get(0)?.ac_id){
                //理疗
                    33->{
                        Glide.with(this)
                                .load(Config.HOST + any?.data?.get(0)?.litpic)
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(cure_img1)
                        Glide.with(this)
                                .load(Config.HOST + any?.data?.get(1)?.litpic)
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(cure_img2)
                        Glide.with(this)
                                .load(Config.HOST + any?.data?.get(2)?.litpic)
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(cure_img3)
                        cure_img1_text.text = any?.data?.get(0)?.title
                        cure_img2_text.text = any?.data?.get(1)?.title
                        cure_img3_text.text = any?.data?.get(2)?.title

                        cure_img1.tag = any?.data?.get(0)?.id
                        cure_img2.tag = any?.data?.get(1)?.id
                        cure_img3.tag = any?.data?.get(2)?.id

                        mIPresenter.getHospital( 34)
                    }
                //合作医院
                    34->{
                        mAdapter.setNewData(any.data)
                    }
                //药膳
                    38->{
                        Glide.with(this)
                                .load(Config.HOST + any?.data?.get(0)?.litpic)
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(special_img1)
                        Glide.with(this)
                                .load(Config.HOST + any?.data?.get(1)?.litpic)
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(special_img2)
                        Glide.with(this)
                                .load(Config.HOST + any?.data?.get(2)?.litpic)
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(special_img3)
                        special_img1_text.text = any?.data?.get(0)?.title
                        special_img2_text.text = any?.data?.get(1)?.title
                        special_img3_text.text = any?.data?.get(2)?.title

                        special_img1.tag =  any?.data?.get(0)?.id
                        special_img2.tag =  any?.data?.get(1)?.id
                        special_img3.tag =  any?.data?.get(2)?.id
                        mIPresenter.getHospital(33,1, 1, 3)
                    }
                }

            }
        }
    }

    override fun setupDagger(appComponent: AppComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .iViewModule(IViewModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_hospital

    override fun initView() {
        hospital_list.adapter = mAdapter
        hospital_list.layoutManager = LinearLayoutManager(getAc())
        hospital_list.addItemDecoration(DividerItemDecoration(getAc(),LinearLayoutManager.VERTICAL))
//        mAdapter.setOnLoadMoreListener(this, hospital_list)
    }

    override fun initData() {
        mIPresenter.getHospital(35,1, 1, 3)
    }

    override fun initEvent() {
        arrayOf(v_medicate_diet, v_cure_book, v_acupuncture, v_consult,
                special_img1, special_img2, special_img3, special_more,
                cure_img1, cure_img2, cure_img3, cure_more, hospital_more).setViewListener(this)

        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, baseViewHolder, position ->
            var id = mAdapter.getItem(position)?.id
            openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/detail/id/$id.html")
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cure_img1,
            R.id.cure_img2,
            R.id.cure_img3,
            R.id.special_img1,
            R.id.special_img2,
            R.id.special_img3 -> {
                if(v.tag!=null) {
                    var id = v.tag
                    openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/detail/id/$id.html")
                }
            }
            R.id.v_consult -> openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/category.html")
            R.id.v_cure_book->openActivity(WebViewActivity::class.java, "url="+"http://1.soowww.com/doctor.html")
            R.id.v_medicate_diet -> openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/medical.html")
            R.id.v_acupuncture -> openActivity(WebViewActivity::class.java, "url=http://4.soowww.com/mobile/article/physica.html")
         }
    }
}