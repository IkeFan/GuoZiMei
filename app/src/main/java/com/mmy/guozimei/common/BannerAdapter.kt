package com.mmy.guozimei.common

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.mmy.guozimei.R
import org.jetbrains.annotations.NotNull

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
class BannerAdapter(@NotNull val resLayout:Int, var mContext:Context) :PagerAdapter(){
    var mData:IntArray? = null
    var currPosition:Int =1
    override fun getCount(): Int {
      return Int.MAX_VALUE
    }

    fun setData(data:IntArray){
        mData = data
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view =   LayoutInflater.from(mContext).inflate(resLayout,container,false)
        var imageView = view.findViewById<ImageView>(R.id.image_cover) as ImageView
        imageView.setImageDrawable(mContext.resources.getDrawable(mData!![position%(mData!!.size)]))
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        container.addView(view)
//        view.alpha = 0.5f
        view.alpha = if( currPosition == position){
            1.0f
        }else{
            0.5f
        }
        view.invalidate()
//        Log.e("BannerAdapter", "BannerAdapter position:$position, current:$currPosition")
        return view
    }

    fun setCurrentPosition(position:Int){
        currPosition = position
        notifyDataSetChanged()
    }
}