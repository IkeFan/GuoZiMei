package com.mmy.guozimei.modules.hospital.adapters

import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.adapter.BaseViewHolder
import com.mmy.frame.data.bean.HospitalBean
import com.mmy.guozimei.R
import kotlinx.android.synthetic.main.adapter_hospital.view.*

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HospitalAdapter(resId:Int): BaseQuickAdapter<HospitalBean.Hospital, BaseViewHolder>(resId) {
    override fun convert(helper: BaseViewHolder?, item: HospitalBean.Hospital?) {
        helper?.setText(R.id.item_name, item?.name)
        helper?.setText(R.id.item_title, item?.title)
        helper?.setText(R.id.item_location, item?.location)
        helper?.setText(R.id.item_payed, item?.payed.toString())
        helper?.setText(R.id.item_common, item?.common.toString())
        helper?.setImageDrawable(R.id.item_logo, mContext.resources.getDrawable(item?.logo!!) )
    }
}