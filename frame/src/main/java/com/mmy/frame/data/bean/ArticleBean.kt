package com.mmy.frame.data.bean

/**
 * Created by fanfuqiang on 2018/10/11.
 */
class ArticleBean : IBean() {
    var data: Data? = null
    class Data{
        var count:Int? = null
        var limit:String? = null
        var data: ArrayList<Article>? = null
    }

    class Article {
        var id: Int? = null
        var ac_id: Int? = null
        var title: String? = null
        var short_title: String? = null
        var author: String? = null
        var litpic: String? = null
        var description: String? = null
        var click: Int? = null
        var publish_time: Int? = null
        var jump_url: String? = null
    }
}