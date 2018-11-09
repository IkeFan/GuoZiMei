package com.mmy.frame.data.bean

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HospitalBean:IBean(){
    var data:List<Hospital>? = null

    class Hospital(var id:Int, var ac_id:Int, var title:String?="", var short_title:String?="", var author:String?="", var litpic:String? = "",
                   var description:String?="", var click:Int?=null, var publish_time:Int, var jump_url:String)
}