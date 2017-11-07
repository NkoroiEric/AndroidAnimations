package com.androidanimations.physicsAnimations



import android.os.Bundle
import android.support.animation.SpringAnimation
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.SeekBar
import com.androidanimations.R

class ButcherArticleActivity : AppCompatActivity() {

    private var downX: Float = 0.toFloat()
    private var downY: Float = 0.toFloat()
    private var damping: SeekBar? = null
    private var stiffness: SeekBar? = null
    private var velocityTracker: VelocityTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buther_article)
        findViewById<View>(android.R.id.content).systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        stiffness = findViewById<View>(R.id.stiffness) as SeekBar
        damping = findViewById<View>(R.id.damping) as SeekBar
        velocityTracker = VelocityTracker.obtain()
        val box = findViewById<View>(R.id.box)
        findViewById<View>(R.id.root).setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    velocityTracker!!.addMovement(event)
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    box.translationX = event.x - downX
                    box.translationY = event.y - downY
                    velocityTracker!!.addMovement(event)
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    velocityTracker!!.computeCurrentVelocity(1000)
                    if (box.translationX != 0f) {
                        val animX = SpringAnimation(box, SpringAnimation.TRANSLATION_X, 0f)
                        animX.spring.stiffness = getStiffness()
                        animX.spring.dampingRatio = getDamping()
                        animX.setStartVelocity(velocityTracker!!.xVelocity)
                        animX.start()
                    }
                    if (box.translationY != 0f) {
                        val animY = SpringAnimation(box, SpringAnimation.TRANSLATION_Y, 0f)
                        animY.spring.stiffness = getStiffness()
                        animY.spring.dampingRatio = getDamping()
                        animY.setStartVelocity(velocityTracker!!.yVelocity)
                        animY.start()
                    }
                    velocityTracker!!.clear()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun getStiffness(): Float {
        return Math.max(stiffness!!.progress.toFloat(), 1f)
    }

    private fun getDamping(): Float {
        return damping!!.progress / 100f
    }
}