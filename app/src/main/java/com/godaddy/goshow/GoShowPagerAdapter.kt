package com.godaddy.goshow

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class GoShowPagerAdapter(private val activity: Activity, private val scenes: List<Scene>): PagerAdapter() {

    override fun getCount(): Int = scenes.size
    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = activity.layoutInflater.inflate(scenes[position].layout, container, false)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}
