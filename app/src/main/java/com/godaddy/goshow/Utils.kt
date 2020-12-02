package com.godaddy.goshow

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.Window
import android.widget.FrameLayout

fun View.getViewCoordinates(activity: Activity): FloatArray{
    val location = IntArray(2)
    getLocationInWindow(location)
    return floatArrayOf(
        (location[0] + (width/2)).toFloat(),
        (location[1] + activity.statusBarHeight() + (height)/2).toFloat()
    )
}

internal fun Activity.statusBarHeight(): Int{
    val rectangle = Rect()
    val window: Window = this.window
    window.decorView.getWindowVisibleDisplayFrame(rectangle)

    val statusBarHeight: Int = rectangle.top
    val contentViewTop: Int = window.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT).top

    return contentViewTop - statusBarHeight
}

internal fun View.getRadius(): Float {
    val h = height
    val w = width
    val diameter = if(h > w) h else w

    return diameter/2.0f
}
