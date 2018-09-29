package com.mmy.guozimei.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView

/**
 * @file       MostGridView.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/29 0029
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class MostGridView(context: Context?, attrs: AttributeSet?) : GridView(context, attrs) {
    /**
     * 设置不滚动
     */
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,
                View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}