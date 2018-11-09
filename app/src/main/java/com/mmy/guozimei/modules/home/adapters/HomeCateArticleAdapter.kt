package com.mmy.guozimei.modules.home.adapters

import com.bumptech.glide.Glide
import com.mmy.frame.adapter.BaseQuickAdapter
import com.mmy.frame.adapter.BaseViewHolder
import com.mmy.frame.data.bean.ArticleCategoryBean
import com.mmy.frame.utils.Config
import com.mmy.guozimei.R

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HomeCateArticleAdapter(resId:Int) :BaseQuickAdapter<ArticleCategoryBean.ArticleCategory, BaseViewHolder>(resId) {
    override fun convert(helper: BaseViewHolder?, item: ArticleCategoryBean.ArticleCategory) {
        helper?.setText(R.id.item_name, item.name)
        var i= 0

        helper?.setText(R.id.left_title, item?.list?.get(0)?.title+">")
        Glide.with(mContext)
                .load(Config.HOST + item?.list?.get(0)?.litpic)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(helper?.getView(R.id.left_img))
        helper?.setText(R.id.left_title, item?.list?.get(1)?.title+">")
        Glide.with(mContext)
                .load(Config.HOST + item?.list?.get(1)?.litpic)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(helper?.getView(R.id.right_img))
        item?.list?.forEach {
            when(i){
                0->{
                    helper?.setText(R.id.left_title, item?.list?.get(0)?.title+">")
                    Glide.with(mContext)
                            .load(Config.HOST + item?.list?.get(0)?.litpic)
                            .error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(helper?.getView(R.id.left_img))
                }
                1->{
                    helper?.setText(R.id.right_title, item?.list?.get(1)?.title+">")
                    Glide.with(mContext)
                            .load(Config.HOST + item?.list?.get(1)?.litpic)
                            .error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(helper?.getView(R.id.right_img))
                }
            }
            i++
        }


        helper?.addOnClickListener(R.id.item_more)
        helper?.addOnClickListener(R.id.left_img)
        helper?.addOnClickListener(R.id.right_img)
//        helper?.setImageDrawable(R.id.item_portrait, mContext.resources.getDrawable(item?.portrait!!))
    }
}