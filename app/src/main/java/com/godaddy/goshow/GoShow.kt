package com.godaddy.goshow

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.goshow_layout.*

class GoShow (
    private val activity: AppCompatActivity,
    private var scenes: List<Scene> = listOf()
) : AlertDialog(activity, R.style.Theme_AppCompat_DayNight_NoActionBar), ViewPager.OnPageChangeListener {
    private var activeSceneIndicatorColor: Int? = null
    private lateinit var sceneIndicator: SceneIndicator
    private lateinit var viewPager: ViewPager
    private lateinit var buttonFinish: TextView
    private lateinit var buttonSkip: TextView
    private var backgroundColor: Int = R.color.transparent
    private var startupDelay = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goshow_layout)

        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewPager = findViewById<ViewPager>(R.id.goShowViewPager)
        viewPager.adapter = GoShowPagerAdapter(activity, scenes)
        viewPager.addOnPageChangeListener(this)

        setupSceneIndicator()
        setupClickListeners()
    }

    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        buttonFinish.enable(position == scenes.size - 1)
        buttonSkip.enable(position < scenes.size - 1)
        sceneIndicator.setCurrentScene(position)
        spotlightContainer.removeAllViews()
        showSpotlight(scenes[position])
    }
    override fun show() {
        start()
    }

    fun start() {
        if (!isShowing) {
            Handler(Looper.getMainLooper()).postDelayed({
                run {
                    super.show()
                    activeSceneIndicatorColor?.let { sceneIndicator.setActiveIndicatorColor(it) }
                    showSpotlight(scenes[0])
                }
            }, startupDelay)
        }
    }

    fun setStartupDelay(delay: Long): GoShow {
        startupDelay = delay
        return this
    }

    fun setBackgroundColor(@ColorRes color: Int): GoShow {
        backgroundColor = color
        return this
    }

    fun setActiveSceneIndicatorColor(@ColorRes color: Int): GoShow {
        activeSceneIndicatorColor = color
        return this
    }

    fun setInactiveSceneIndicatorColor(@ColorRes color: Int): GoShow {
        sceneIndicator.setInactiveIndicatorColor(color)
        return this
    }

    fun setNavigationTextColor(@ColorRes color: Int): GoShow {
        val textColor = ContextCompat.getColor(context, color)
        buttonSkip.setTextColor(textColor)
        buttonFinish.setTextColor(textColor)
        return this
    }

    private fun showSpotlight(scene: Scene) {
        val spotlight = Spotlight(activity, scene.targetView, scene.spotlightRadius, backgroundTintColor = backgroundColor)
        spotlightContainer.addView(
            spotlight,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        spotlight.animateSpotlight()
    }

    private fun setupSceneIndicator() {
        sceneIndicator = findViewById<SceneIndicator>(R.id.sceneIndicator)
        sceneIndicator.setSceneCount(scenes.size)
    }

    private fun setupClickListeners() {
        buttonFinish = findViewById(R.id.buttonFinish)
        buttonFinish.setOnClickListener {
            this@GoShow.dismiss()
        }

        buttonSkip = findViewById(R.id.buttonSkip)
        buttonSkip.setOnClickListener {
            this@GoShow.dismiss()
        }
    }

    private fun View.enable(enable: Boolean) {
        isEnabled = enable
        visibility = if (enable) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }
}
