package com.godaddy.goshow

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

@SuppressLint("ViewConstructor")
class Spotlight (
    activity: Activity,
    target: View,
    spotlightRadius: Int,
    @ColorRes backgroundTintColor: Int = R.color.transparent,
    @ColorRes spotlightColor: Int = android.R.color.white
) : View(activity) {
    private var circlePaint: Paint = Paint()
    private var rectPaint: Paint  = Paint()
    private var circleCenterX = 0.0f
    private var circleCenterY = 0.0F
    var radius = 50.0f

    init {
        circlePaint.color = ContextCompat.getColor(activity, spotlightColor)
        circlePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        circlePaint.isAntiAlias = true
        rectPaint.color = ContextCompat.getColor(activity, backgroundTintColor)
        rectPaint.isAntiAlias = true

        setLayerType(LAYER_TYPE_SOFTWARE, null)

        radius = target.getRadius() + spotlightRadius.toFloat()

        val targetViewCoordinates = target.getViewCoordinates(activity)
        circleCenterX = targetViewCoordinates[0]
        circleCenterY = targetViewCoordinates[1]
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val h = height
        val w = width

        canvas?.drawRect(0f, 0f, w.toFloat(), h.toFloat(), rectPaint)
        canvas?.drawCircle(circleCenterX, circleCenterY, radius, circlePaint)

        invalidate()
    }

    fun animateSpotlight(){
        ObjectAnimator.ofFloat(this, "radius", 0.0f, radius).apply {
            duration = 500
            start()
        }
    }

    fun getCenter(): FloatArray {
        val centerCoordinates = FloatArray(2)
        centerCoordinates[0] = circleCenterX
        centerCoordinates[1] = circleCenterY

        return centerCoordinates
    }
}
