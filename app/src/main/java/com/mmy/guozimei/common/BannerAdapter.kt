package com.mmy.guozimei.common

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mmy.frame.data.bean.BannerBean
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R


/**
 * @file       BannerAdapter.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/11 0011
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class BannerAdapter(private var mContext: Context, private var mData: ArrayList<BannerBean.Banner>, var pagerView:ViewPager) : PagerAdapter() {
    var currPosition: Int = 1
    var mCacheViews = ArrayList<View>()
    override fun getCount(): Int {
        return Int.MAX_VALUE/2
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        pagerView.removeView(`object`)
        mCacheViews.add(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view:View
        if (mCacheViews.isEmpty()) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_banner, null, false)
            var imageView = view.findViewById<ImageView>(R.id.image_cover) as ImageView
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }else{
            view =  mCacheViews.removeAt(0)
        }
//        if (view?.parent != null) {
//            var parent = view?.parent as ViewGroup
//            parent.removeView(view)
//        }

        Glide.with(mContext)
                .load(Config.HOST + mData[position % mData.size].content)
                .error(R.mipmap.banner_bg)
                .placeholder(R.mipmap.banner_bg)
                .into(view.findViewById(R.id.image_cover))
        container.addView(view)
//        view.alpha = 0.5f
//        view?.alpha = if (currPosition == position) {
//            1.0f
//        } else {
//            0.5f
//        }
        return view!!
    }

    fun setCurrentPosition(position: Int) {
        currPosition = position
        notifyDataSetChanged()
    }

}