package com.mmy.guozimei.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @file       LetterListView.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/28 0028
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class LetterListView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    internal var b = arrayOf("定位",
            //            "最近",
            "热门", "全部", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    internal var choose = -1
    internal var paint = Paint()
    internal var showBkg = false


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (showBkg) {
            canvas?.drawColor(Color.parseColor("#40000000"))
        }
        val height = height
        val width = width
        val singleHeight = height / b.size
        for (i in b.indices) {
            paint.color = Color.parseColor("#8c8c8c")
            paint.textSize = 18f
            // paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.isAntiAlias = true
            /*if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}*/
            val xPos = width / 2 - paint.measureText(b[i]) / 2
            val yPos = (singleHeight * i + singleHeight).toFloat()
            canvas?.drawText(b[i], xPos, yPos, paint)
            paint.reset()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.getAction()
        val y = event?.y
        val oldChoose = choose
        val listener = onTouchingLetterChangedListener
        val c = (y!!/height * b.size).toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                showBkg = true
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.size) {
                        listener(b[c])
                        choose = c
                        invalidate()
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> if (oldChoose != c && listener != null) {
                if (c >= 0 && c < b.size) {
                    listener(b[c])
                    choose = c
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                showBkg = false
                choose = -1
                invalidate()
            }
        }
        return true
    }

    var onTouchingLetterChangedListener:(String)->Unit ={letter->

    }
}