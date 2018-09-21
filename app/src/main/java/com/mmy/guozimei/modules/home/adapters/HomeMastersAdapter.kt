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
}