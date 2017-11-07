package com.androidanimations.curvedMotion

import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.androidanimations.R
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_curved_motion.*


/**
 * This app shows how to move a view in a curved path between two endpoints.
 * The real work is done by PathEvaluator, which interpolates along a path
 * using Bezier control and anchor points in the path.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on the DevBytes playlist in the androiddevelopers channel on YouTube at
 * https://www.youtube.com/playlist?list=PLWz5rJ2EKKc_XOgcRukSoKKjewFJZrKV0.
 */
class CurvedMotionActivity : AppCompatActivity() {

    var mTopLeft = true
    lateinit var mButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curved_motion)
        mButton = button
        mButton.setOnClickListener { v ->
            //Capture current location of button
            val oldLeft = mButton.left
            val oldTop = mButton.top

            //change layout parameters of button to move it
            moveButton()

            //Add OnPreDrawListener to catch button after layout but before drawing
            mButton.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    mButton.viewTreeObserver.removeOnPreDrawListener(this)

                    // Capture new location
                    val left = mButton.left
                    val top = mButton.top
                    val deltaX = left - oldLeft
                    val deltaY = top - oldTop

                    // Set up path to new location using a B�zier spline curve
                    val path = AnimatorPath()
                    path.moveTo((-deltaX).toFloat(), (-deltaY).toFloat())
                    path.curveTo((-(deltaX / 2)).toFloat(), (-deltaY).toFloat(), 0f, (-deltaY / 2).toFloat(), 0f, 0f)

                    // Set up the animation
                    val anim = ObjectAnimator.ofObject(
                            this, "buttonLoc",
                            PathEvaluator(), path.getPoints().toTypedArray())
                    anim.interpolator = sDecelerateInterpolator
                    anim.start()
                    return true
                }
            })
        }
    }

    private fun moveButton() {
        val params:RelativeLayout.LayoutParams = mButton.layoutParams as RelativeLayout.LayoutParams
        if (mTopLeft){
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
            params.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        }else{
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        }
        mButton.layoutParams = params
        mTopLeft = !mTopLeft
    }

    /**
     * We need this setter to translate between the information the animator
     * produces (a new "PathPoint" describing the current animated location)
     * and the information that the button requires (an xy location). The
     * setter will be called by the ObjectAnimator given the 'buttonLoc'
     * property string.
     */
    fun setButtonLoc(newLoc : PathPoint){
        mButton.translationX = newLoc.mX
        mButton.translationY = newLoc.mY
    }

    companion object {
        private val sDecelerateInterpolator = DecelerateInterpolator()
    }
}
