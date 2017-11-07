package com.androidanimations.toonGame

import android.animation.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.animation.*
import com.androidanimations.R
import android.widget.Button
import kotlinx.android.synthetic.main.toon_game_fragment.*


/**
 * Created by scaleup on 06/11/17.
 */

/**
 * This application shows various cartoon animation techniques in the context of
 * a larger application, to show how such animations might be used to create a more
 * interactive, fun, and engaging experience.
 *
 * This main activity launches a sub-activity when the Play button is clicked. The
 * main action in this master activity is bouncing the Play button in, randomly
 * bouncing it while waiting for input, and animating its press and click behaviors
 * when the user interacts with it.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on the DevBytes playlist in the androiddevelopers channel on YouTube at
 * https://www.youtube.com/playlist?list=PLWz5rJ2EKKc_XOgcRukSoKKjewFJZrKV0.
 */
class ToonGameActivity : AppCompatActivity() {

    lateinit var mStarter : Button
    lateinit var mContainer : ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toon_game_fragment)
        overridePendingTransition(0,0)
        mStarter = startButton as Button
        mContainer = container as ViewGroup
        mStarter.setOnTouchListener(funButtonListener)
        mStarter.animate().duration = 100
    }

    override fun onResume() {
        super.onResume()
        mContainer.scaleX = 1f
        mContainer.scaleY = 1f
        mContainer.alpha =1f
        mStarter.visibility = View.INVISIBLE
        mContainer.viewTreeObserver.addOnPreDrawListener(mOnPreDrawListener)
    }

    override fun onPause() {
        super.onPause()
        mStarter.removeCallbacks(mSquashRunnable)
    }

    private val mSquashRunnable = Runnable {
        squishyBounce(mStarter, 0F,
                (mContainer.height - mStarter.top - mStarter.height).toFloat(),
                0f, .5f, 1.5f);
    }


    private val funButtonListener: View.OnTouchListener = object : View.OnTouchListener{
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    mStarter.animate().scaleX(.8f).scaleY(.8f).interpolator = sDecelerator
                    mStarter.setTextColor(Color.CYAN)
                    mStarter.removeCallbacks(mSquashRunnable)
                    mStarter.isPressed = true
                }

                MotionEvent.ACTION_MOVE -> {
                    val x : Float = event.x
                    val y : Float = event.y
                    val isInside = (x > 0 && x < mStarter.width &&
                            y > 0 && y < mStarter.height)
                    if (mStarter.isPressed != isInside){
                        mStarter.isPressed = isInside
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (mStarter.isPressed){
                        mStarter.performClick()
                        mStarter.isPressed = false
                    }else {
                        mStarter.animate().scaleX(1f).scaleY(1f).interpolator = sAccelerator
                    }
                    mStarter.setTextColor(Color.BLUE)
                }
            }
            return true
        }

    }

   fun play(view: View){
        mContainer.animate().scaleX(5f).scaleY(5f).alpha(0f).setDuration(LONG_DURATION).setDuration(LONG_DURATION)
                .withEndAction {
                    mStarter.postOnAnimation {
                        val intent = Intent(this, PlayerSetupActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0,0)
                    }
                }
        view.removeCallbacks(mSquashRunnable)
    }

    private val mOnPreDrawListener : ViewTreeObserver.OnPreDrawListener = object : ViewTreeObserver.OnPreDrawListener{
        override fun onPreDraw(): Boolean {
            mContainer.viewTreeObserver.removeOnPreDrawListener(this)
            mContainer.postDelayed(Runnable {
                // Drop in the button from off the top of the screen
                mStarter.setVisibility(View.VISIBLE);
                mStarter.y = -mStarter.height.toFloat()
                squishyBounce(mStarter,
                        (-(mStarter.top + mStarter.height)).toFloat(),
                        (mContainer.height - mStarter.top - mStarter.height).toFloat(),
                        0f,.5f, 1.5f)
            },500)
            return true
        }
    }

    private fun squishyBounce(view: View, startTY: Float, bottomTY: Float, endTY: Float, squash: Float, stretch: Float) {
        view.pivotX = (view.width /2).toFloat()
        view.pivotY = view.height.toFloat()
        var pvhTY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,
                startTY, bottomTY)
        var pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, .7f)
        var pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f)
        val downAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhTY, pvhSX, pvhSY)
        downAnim.interpolator = sAccelerator

        pvhTY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, bottomTY, endTY)
        pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)

        val upAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhTY, pvhSX,pvhSY)
        upAnim.interpolator = sDecelerator

        pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, stretch)
        pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, squash)
        val stretchAnim = ObjectAnimator.ofPropertyValuesHolder(view , pvhSX, pvhSY)
        stretchAnim.repeatCount = 1
        stretchAnim.repeatMode = ValueAnimator.REVERSE
        stretchAnim.interpolator = sDecelerator

        val set = AnimatorSet()
        set.playSequentially(downAnim, stretchAnim, upAnim)
        set.duration = getDuration(SHORT_DURATION)
        set.start()
        set.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animator: Animator){
                view.postDelayed(mSquashRunnable, (500 + Math.random() * 2000).toLong())
            }
        })
    }


    private fun getDuration(baseDuration : Long): Long {
        return (baseDuration * sDurationScale).toLong()
    }


    companion object {
        var SHORT_DURATION: Long = 100
        var MEDIUM_DURATION: Long = 200
        var REGULAR_DURATION: Long = 300
        var LONG_DURATION: Long = 500
        private val sDurationScale = 1f
        private val sAccelerator = AccelerateInterpolator()
        private val sDecelerator = DecelerateInterpolator()
        private val sLinearInterpolator = LinearInterpolator()
    }
}