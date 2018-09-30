package com.mmy.guozimei.modules.store.adapters

import android.support.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.adapter.BaseViewHolder
import com.mmy.frame.data.bean.StoreBean
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R

/**
 * @file       StoreProductAdapter.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/7 0007
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class StoreProductAdapter(@LayoutRes id:Int): BaseQuickAdapter<StoreBean.Product, BaseViewHolder>(id) {
    override fun convert(helper: BaseViewHolder?, item: StoreBean.Product?) {
        helper?.setText(R.id.item_name, item?.goods_name)
        helper?.setText(R.id.item_title, "【"+item?.goods_name+"】"+item?.goods_remark)
//        helper?.setText(R.id.item_place, item?.place)
        helper?.setText(R.id.item_price, item?.market_price)

        Glide.with(mContext)
                .load(Config.HOST+item?.logo)
                .placeholder(R.mipmap.ic_default_portrait)
                .error(R.mipmap.ic_default_portrait)
                .into(helper?.getView(R.id.item_img))
    }
}