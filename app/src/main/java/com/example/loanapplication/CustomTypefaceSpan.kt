package com.example.loanapplication

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class CustomTypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {

    override fun updateMeasureState(textPaint: TextPaint) {
        applyCustomTypeface(textPaint, typeface)
    }

    override fun updateDrawState(textPaint: TextPaint) {
        applyCustomTypeface(textPaint, typeface)
    }

    private fun applyCustomTypeface(paint: Paint, tf: Typeface) {
        val oldTypeface = paint.typeface
        val oldStyle = oldTypeface?.style ?: 0

        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }

        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }

        paint.typeface = tf
    }
}
