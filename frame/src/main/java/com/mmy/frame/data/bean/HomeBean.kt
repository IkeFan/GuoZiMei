package com.mmy.frame.data.bean

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HomeBean: IBean() {
    var data:List<GreatMaster>? = null

    class GreatMaster(var id:Int? = 0,
        var name:String? = null,
        var title:String? = null,
        var honor:String? = null,
        var male:Int? = 1,
        var age:Int? = 0,
        var type:String? = null,
        var logo:String? =null,
        var description:String? = null
    )
}