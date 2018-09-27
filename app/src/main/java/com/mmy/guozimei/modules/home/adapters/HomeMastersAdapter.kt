package com.mmy.guozimei.modules.home.adapters

import com.bumptech.glide.Glide
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.adapter.BaseViewHolder
import com.mmy.frame.data.bean.HomeBean
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HomeMastersAdapter(resId:Int) :BaseQuickAdapter<HomeBean.GreatMaster, BaseViewHolder>(resId) {
    override fun convert(helper: BaseViewHolder?, item: HomeBean.GreatMaster?) {
        helper?.setText(R.id.item_name, item?.name)
        helper?.setText(R.id.item_achievements, item?.description)
        helper?.setText(R.id.item_level,item?.title)
        helper?.setText(R.id.item_type, item?.type)
        helper?.setText(R.id.item_honor, item?.honor)
        helper?.setText(R.id.item_age, item?.age.toString())
        helper?.setImageResource(R.id.item_male_iv, when(item?.male){
            1-> R.mipmap.ic_male
            2-> R.mipmap.female
            else -> 0
        })
        Glide.with(mContext)
                .load(Config.HOST + item?.logo)
                .error(R.mipmap.ic_default_portrait)
                .placeholder(R.mipmap.ic_default_portrait)
                .into(helper?.getView(R.id.item_portrait))
//        helper?.setImageDrawable(R.id.item_portrait, mContext.resources.getDrawable(item?.portrait!!))
    }
}