package com.mmy.frame.data.bean

/**
 * Created by fanfuqiang on 2018/10/11.
 */
class ArticleCategoryBean : IBean() {
    var data: ArrayList<ArticleCategory>? = null

    class ArticleCategory {
        var id: Int? = null
        var name: String? = null
        var list: ArrayList<ArticleBean.Article>? = null
    }
}