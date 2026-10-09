package com.hewking.halo

import android.graphics.Paint
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View

/**
 * 类的描述：
 * 创建人员：hewking
 * 创建时间：2018/12/28
 * 修改人员：hewking
 * 修改时间：2018/12/28
 * 修改备注：
 * Version: 1.0.0
 */

@ColorInt
fun View.getColor(@ColorRes resid: Int): Int {
    return ContextCompat.getColor(context, resid)
}

fun View.dp2px(dp: Float): Int {
    return dpToPx(dp, resources.displayMetrics.density)
}

fun Paint.textHeight(): Float {
    return paintTextHeight(descent(), ascent())
}