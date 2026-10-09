package com.hewking.halo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Locks the drawing contract used by HaloProgressView: density rounding,
 * halo breathing, finish reveal, and progress-label placement.
 */
class HaloProgressMathTest {

    @Test
    fun dpToPx_usesHalfUpTruncationForPositiveDp() {
        assertEquals(50, dpToPx(50f, 1f))
        assertEquals(40, dpToPx(40f, 1f))
        assertEquals(20, dpToPx(10f, 2f))
        assertEquals(1, dpToPx(1.25f, 1f))
        assertEquals(2, dpToPx(1.5f, 1f))
        assertEquals(0, dpToPx(50f, 0f))
    }

    @Test
    fun dpToPx_defaultHaloRadiiFollowDisplayDensity() {
        assertEquals(10f, defaultCornerRadius(1f), 0.001f)
        assertEquals(15f, defaultCornerRadius(1.5f), 0.001f)
        assertEquals(20f, defaultCornerRadius(2f), 0.001f)
        assertEquals(30f, defaultCornerRadius(3f), 0.001f)
        assertEquals(40f, defaultCornerRadius(4f), 0.001f)
    }

    @Test
    fun dpToPx_negativeValuesTruncateTowardZero() {
        assertEquals(0, dpToPx(-0.25f, 1f))
        assertEquals(-1, dpToPx(-1.5f, 1f))
    }

    @Test
    fun paintTextHeight_isDescentMinusAscent() {
        assertEquals(16f, paintTextHeight(4f, -12f), 0.001f)
        assertEquals(0f, paintTextHeight(0f, 0f), 0.001f)
    }

    @Test
    fun cornerRadius_isOuterMinusInner() {
        assertEquals(10f, cornerRadius(50f, 40f), 0.001f)
        assertEquals(0f, cornerRadius(45f, 45f), 0.001f)
        assertEquals(-5f, cornerRadius(40f, 45f), 0.001f)
    }

    @Test
    fun progressLabel_formatsIntegerPercentWithoutPadding() {
        assertEquals("0%", progressLabel(0))
        assertEquals("100%", progressLabel(100))
        assertEquals("7%", progressLabel(7))
    }

    @Test
    fun progressLabel_keepsOutOfRangeValuesUnclamped() {
        assertEquals("-1%", progressLabel(-1))
        assertEquals("101%", progressLabel(101))
    }

    @Test
    fun haloRadius_interpolatesBetweenInnerAndOuter() {
        assertEquals(40f, haloRadius(40f, 50f, 0f), 0.001f)
        assertEquals(45f, haloRadius(40f, 50f, 0.5f), 0.001f)
        assertEquals(50f, haloRadius(40f, 50f, 1f), 0.001f)
        assertEquals(42.5f, haloRadius(40f, 50f, 0.25f), 0.001f)
    }

    @Test
    fun haloRadius_invertedOrEqualRadiiStayContinuous() {
        assertEquals(45f, haloRadius(50f, 40f, 0.5f), 0.001f)
        assertEquals(30f, haloRadius(30f, 30f, 0f), 0.001f)
        assertEquals(30f, haloRadius(30f, 30f, 1f), 0.001f)
    }

    @Test
    fun haloAlpha_truncatesAndDoesNotClamp() {
        assertEquals(0, haloAlpha(0f))
        assertEquals(127, haloAlpha(0.5f))
        assertEquals(255, haloAlpha(1f))
    }

    @Test
    fun revealMaxRadius_isHalfTheViewDiagonal() {
        assertEquals(0.0, revealMaxRadius(0, 0), 0.000001)
        assertEquals(2.5, revealMaxRadius(3, 4), 0.000001)
        assertEquals(50.0, revealMaxRadius(100, 0), 0.000001)
        assertEquals(50.0, revealMaxRadius(0, 100), 0.000001)
    }

    @Test
    fun revealRadius_movesFromOuterRadiusToCoverTheView() {
        val maxRadius = revealMaxRadius(6, 8)
        assertEquals(5.0, maxRadius, 0.000001)
        assertEquals(20f, revealRadius(20f, maxRadius, 0f), 0.001f)
        assertEquals(5f, revealRadius(20f, maxRadius, 1f), 0.001f)
        assertEquals(12.5f, revealRadius(20f, maxRadius, 0.5f), 0.001f)
    }

    @Test
    fun finishRevealStartsWhereTheExpandedHaloEnds() {
        val outRadius = 60f
        val innRadius = 45f
        val maxRadius = revealMaxRadius(200, 120)
        assertEquals(outRadius, haloRadius(innRadius, outRadius, 1f), 0.001f)
        assertEquals(outRadius, revealRadius(outRadius, maxRadius, 0f), 0.001f)
        assertEquals(maxRadius, revealRadius(outRadius, maxRadius, 1f).toDouble(), 0.001)
    }

    @Test
    fun progressText_isCenteredOnTheViewOrigin() {
        val textWidth = 18f
        assertEquals(0f, progressTextOffsetX(textWidth) + textWidth / 2f, 0.001f)
        assertEquals(-5.5f, progressTextOffsetX(11f), 0.001f)
        assertEquals(0f, progressTextOffsetX(0f), 0.001f)
    }

    @Test
    fun progressTextBaseline_subtractsDescentSoGlyphsStayCentered() {
        val descent = 4f
        val textHeight = paintTextHeight(descent, -12f)
        val baseline = progressTextOffsetY(textHeight, descent)
        assertEquals(16f, textHeight, 0.001f)
        assertEquals(4f, baseline, 0.001f)
        assertTrue(baseline < textHeight / 2f)
    }

    private fun defaultCornerRadius(density: Float): Float {
        return cornerRadius(dpToPx(50f, density).toFloat(), dpToPx(40f, density).toFloat())
    }
}
