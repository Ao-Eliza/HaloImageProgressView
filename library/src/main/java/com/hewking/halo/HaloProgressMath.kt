package com.hewking.halo

/**
 * Drawing math used by [HaloProgressView].
 * Android-free so host unit tests can lock the geometry contract.
 */
internal fun dpToPx(dp: Float, density: Float): Int {
    return (density * dp + 0.5).toInt()
}

internal fun paintTextHeight(descent: Float, ascent: Float): Float {
    return descent - ascent
}

internal fun cornerRadius(outRadius: Float, innRadius: Float): Float {
    return outRadius - innRadius
}

internal fun progressLabel(progress: Int): String {
    return "${progress}%"
}

internal fun haloRadius(innRadius: Float, outRadius: Float, animatorValue: Float): Float {
    return innRadius + (outRadius - innRadius) * animatorValue
}

internal fun haloAlpha(animatorValue: Float): Int {
    return (animatorValue * 255).toInt()
}

internal fun revealMaxRadius(width: Int, height: Int): Double {
    return Math.sqrt(Math.pow(width.toDouble(), 2.0) + Math.pow(height.toDouble(), 2.0)).div(2)
}

internal fun revealRadius(outRadius: Float, maxRadius: Double, finishAnimValue: Float): Float {
    return (outRadius + (maxRadius - outRadius) * finishAnimValue).toFloat()
}

internal fun progressTextOffsetX(textWidth: Float): Float {
    return 0 - textWidth.div(2)
}

internal fun progressTextOffsetY(textHeight: Float, descent: Float): Float {
    return textHeight.div(2) - descent
}
