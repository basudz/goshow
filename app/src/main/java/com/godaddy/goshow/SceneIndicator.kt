package com.godaddy.goshow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.min

class SceneIndicator : View {
    private var activeIndicatorPaint: Paint? = null
    private var inactiveIndicatorPaint: Paint? = null
    private var radius = 0
    private var diameter = 0
    private var currentPosition = 0
    private var sceneCount = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        activeIndicatorPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.active_indicator)
            isAntiAlias = true
        }

        inactiveIndicatorPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.inactive_indicator)
            isAntiAlias = true
        }

        radius = resources.getDimensionPixelSize(R.dimen.indicator_size)
        diameter = radius * 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        inactiveIndicatorPaint?.let {
            for (i in 0 until sceneCount) {
                canvas.drawCircle(radius + (diameter * i).toFloat(), radius.toFloat(), radius / 2.toFloat(), it)
            }
        }

        activeIndicatorPaint?.let {
            canvas.drawCircle(radius + (diameter * currentPosition).toFloat(), radius.toFloat(), radius / 2.toFloat(), it)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getWidth(widthMeasureSpec),
            getHeight(heightMeasureSpec)
        )
    }

    private fun getWidth(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = diameter * sceneCount
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    private fun getHeight(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = 2 * radius + paddingTop + paddingBottom
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }


    fun setCurrentScene(position: Int) {
        currentPosition = position
        invalidate()
    }

    fun setSceneCount(size: Int) {
        sceneCount = size
        invalidate()
    }

    fun setInactiveIndicatorColor(@ColorRes color: Int) {
        inactiveIndicatorPaint?.color = ContextCompat.getColor(context, color)
        invalidate()
    }

    fun setActiveIndicatorColor(@ColorRes color: Int) {
        activeIndicatorPaint?.color = ContextCompat.getColor(context, color)
        invalidate()
    }
}
