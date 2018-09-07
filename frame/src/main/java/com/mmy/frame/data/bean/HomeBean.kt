package com.mmy.frame.data.bean

import android.support.annotation.DrawableRes
import java.io.Serializable

/**
 * Created by fanfuqiang on 2018/9/7.
 */
class HomeBean: IBean() {

    class GreatMaster(
        var name:String? = null,
        var level:Int? = 0,
        var honor:String? = null,
        var achievements:String ? = null,
        var male:Int? = 1,
        var age:Int? = 0,
        var type:Int? = 1,
        @DrawableRes var portrait:Int? = null
    )
}