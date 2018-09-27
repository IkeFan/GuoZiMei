package com.mmy.frame.data.bean

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HomeBean: IBean() {
    var data:List<GreatMaster>? = null
    class GreatMaster(
        var name:String? = null,
        var level:Int? = 0,
        var honor:String? = null,
        var achievements:String ? = null,
        var male:Int? = 1,
        var age:Int? = 0,
        var type:Int? = 1,
        var logo:String? =null,
        var description:String? = null
    )
}