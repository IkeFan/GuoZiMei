package com.mmy.guozimei.modules.hospital.adapters

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.adapter.BaseViewHolder
import com.mmy.frame.data.bean.HospitalBean
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R


/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HospitalAdapter(resId:Int): BaseQuickAdapter<HospitalBean.Hospital, BaseViewHolder>(resId) {
    override fun convert(helper: BaseViewHolder?, item: HospitalBean.Hospital?) {
        helper?.setText(R.id.item_name, item?.title)
//        helper?.setText(R.id.item_title, item?.short_title)
//        helper?.setText(R.id.item_location, item?.location)
//        helper?.setText(R.id.item_payed, item?.payed.toString())
        helper?.setText(R.id.description, item?.description)
        Glide.with(mContext)
                .load(Config.HOST + item?.litpic)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(helper?.getView<ImageView>(R.id.item_logo))
    }
}