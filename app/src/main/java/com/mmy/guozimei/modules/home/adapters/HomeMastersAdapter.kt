package com.mmy.guozimei.modules.home.adapters

import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.adapter.BaseViewHolder
import com.mmy.frame.data.bean.HomeBean
import com.mmy.guozimei.R

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HomeMastersAdapter(resId:Int) :BaseQuickAdapter<HomeBean.GreatMaster, BaseViewHolder>(resId) {
    override fun convert(helper: BaseViewHolder?, item: HomeBean.GreatMaster?) {
        helper?.setText(R.id.item_name, item?.name)
        helper?.setText(R.id.item_achievements, item?.achievements)
        helper?.setText(R.id.item_level, when(item?.level){
            1 -> mContext.getString(R.string.master_primary)
            2 -> mContext.getString(R.string.master_middle)
            3 -> mContext.getString(R.string.master_high)
            else -> mContext.getString(R.string.master_primary)
        })
        helper?.setText(R.id.item_type, when(item?.type){
            1 -> mContext.getString(R.string.painting_master)
            2 -> mContext.getString(R.string.written_master)
            3 -> mContext.getString(R.string.noun_master)
            else -> mContext.getString(R.string.painting_master)
        })
        helper?.setText(R.id.item_age, item?.age.toString())
        helper?.setImageDrawable(R.id.item_portrait, mContext.resources.getDrawable(item?.portrait!!))
    }

    fun initVirData(){
        setNewData(arrayListOf(
                HomeBean.GreatMaster("王明明",2, "2011年4月被聘任为北大国画教授...",
                         "代表作《草原上的阿里》《阿尔卑斯之冬》",1, 47,1, R.mipmap.demo_portrait1),

                HomeBean.GreatMaster("张飙",3, "2013年6月被授予为全国10大书法大师之一...",
                        "代表作《大展宏图》《草书行书录》",1, 76,2, R.mipmap.demo_portrait2),

                HomeBean.GreatMaster("杨璐梦",2, "2012年8月被聘任北京艺术学院教授...",
                        "代表作《青春进行曲》《夜章旋律》",2, 68,3, R.mipmap.demo_portrait3),

                HomeBean.GreatMaster("欧律明",2, "2011年4月被聘任为国务院参事...",
                        "代表作《秋之枫》《落日余晖》",1, 39,1, R.mipmap.ic_default_portrait)
        ))
    }
}