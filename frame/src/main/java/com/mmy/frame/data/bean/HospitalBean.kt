package com.mmy.frame.data.bean

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HospitalBean:IBean(){
    var data:List<Hospital>? = null

    class Hospital(var name:String?="", var payed:Int?=0, var logo:Int?=0, var leve:Int? = 0,
                   var common:Int?=0, var title:String?="", var location:String?="")
}