package com.androidanimations.anticipation

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.graphics.RectF
import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.MotionEvent
import android.view.View


/**
 * Created by scaleup on 07/11/17.
 */

/**
 * Custom button which can be deformed by skewing the top left and right, to simulate
 * anticipation and follow-through animation effects. Clicking on the button runs
 * an animation which moves the button left or right, applying the skew effect to the
 * button. The logic of drawing the button with a skew transform is handled in the
 * draw() override.
 */
class AnticiButton : Button{
    private var mSkewX = 0f
    lateinit var downAnim: ObjectAnimator
    var mOnLeft = true
    var mTempRect = RectF()

    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle){
        init()
    }
    private fun init(){
        setOnTouchListener(mTouchListener)
        setOnClickListener{v ->
            runClickAnim()
        }
    }
    /**
     * skew effect is handled by changing the transform of the canvas
     * and then calling the usual superclass draw() method
     */
    override fun draw(canvas: Canvas) {
        if (mSkewX != 0f){
            canvas.translate(0f, height.toFloat())
            canvas.skew(mSkewX, 0f)
            canvas.translate(0f, -height.toFloat())
        }
        super.draw(canvas)
    }
    /**
     * Anticipate the future animation by rearing back, away from the direction of travel
     */
    private fun runPressAnim(){
        downAnim = ObjectAnimator.ofFloat(this, "skewX", if (mOnLeft) .5f else -.5f)
        downAnim.duration = 2500
        downAnim.interpolator = sDecelerator
        downAnim.start()
    }
    /**
     * Finish the anticipation animation (skew the button back from the direction of
     * travel), animate it to the other side of the screen, then un-skew the button
     * with an OverShoot effect
     */
    private fun runClickAnim() {
        //Anticipation
        var finishDownAnim: ObjectAnimator? = null
        if (downAnim.isRunning){
            //finish the skew animation quickly
            downAnim.cancel()
            finishDownAnim = ObjectAnimator.ofFloat(this, "skewX",
                    if (mOnLeft) .5f else -.5f)
            finishDownAnim.duration = 150
            finishDownAnim.interpolator = sQuickDecelerator
        }

        //Slide. Use LinearInterpolator in this rare situation where we want to start
        //and end fast (no acceleration or deceleration, since we 're doing that part
        //during the anticipation and overshoot phases).
        val moveAnim = ObjectAnimator.ofFloat(this,
                View.TRANSLATION_X, if (mOnLeft) 400f else 0f)
        moveAnim.interpolator = sLinearInterpolator
        moveAnim.duration = 150

        //Then overshoot by stopping the movement but skewing the button as if it couldn't
        //all stop at once
        val skewAnim = ObjectAnimator.ofFloat(this, "skewX",
                if (mOnLeft) -.5f else .5f)
        skewAnim.interpolator = sQuickDecelerator
        skewAnim.duration = 100
        //and wobble it
        val wobbleAnim = ObjectAnimator.ofFloat(this, "skewX",
                0f)
        wobbleAnim.interpolator = sOvershooter
        wobbleAnim.duration = 150

        val set = AnimatorSet()
        set.playSequentially(moveAnim, skewAnim, wobbleAnim)
        if (finishDownAnim != null){
            set.play(finishDownAnim).before(moveAnim)
        }
        set.start()
        mOnLeft = !mOnLeft
    }

    /**
     * Restore the button to its un-pressed state
     */
    private fun runCancelAnim(){
        if(downAnim.isRunning){
            downAnim.cancel()
            val reverser = ObjectAnimator.ofFloat(this, "skewX", 0f)
            reverser.duration = 200
            reverser.interpolator = sAccelerator
            reverser.start()
//            downAnim = null
        }
    }
    /**
     * Handle touch events directly since we want to react on down/up events, not just
     * buttons clicks
     */
    private val mTouchListener: View.OnTouchListener = OnTouchListener { v, event ->
        when(event.action){
            MotionEvent.ACTION_UP -> {
                if (isPressed){
                    performClick()
                    isPressed = false
                }
            }
            //No click: Fall through; equivalent to cancel event
            MotionEvent.ACTION_CANCEL -> {
                //Run the cancel animation in either case
                runCancelAnim()
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                val isInside = (x > 0 && x < width && y > 0 && y < height)

                if (isPressed != isInside){
                    isPressed = isInside
                }
            }
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                runPressAnim()
            }
        }
        true
    }

    /**
     * Sets the amount of left/right skew on the button, which determines how far the button
     * leans
     */
    fun setSkewX(value : Float){
        if (value != mSkewX){
            mSkewX =value
            invalidate()            //force button to redraw with new skew value
            invalidateSkewedBounds()//also invalidate appropriate area of parent
        }
    }
    /**
     * Need to invalidate proper area of parent for skewed bounds
     */
    private fun invalidateSkewedBounds() {
        if (mSkewX != 0f){
            val matrix = Matrix()
            matrix.setSkew(-mSkewX, 0f)
            mTempRect.set(0f,0f,right.toFloat(), bottom.toFloat())
            matrix.mapRect(mTempRect)
            mTempRect.offset(left.toFloat() + translationX,top.toFloat() + translationY)
            (parent as View).invalidate(mTempRect.left.toInt(), mTempRect.top.toInt(),
                    (mTempRect.right + .5f).toInt(), (mTempRect.top + .5f).toInt())
        }
    }
    companion object {
        private val sLinearInterpolator = LinearInterpolator()
        private val sDecelerator = DecelerateInterpolator(8f)
        private val sAccelerator = AccelerateInterpolator()
        private val sOvershooter = OvershootInterpolator()
        private val sQuickDecelerator = DecelerateInterpolator()
    }
}