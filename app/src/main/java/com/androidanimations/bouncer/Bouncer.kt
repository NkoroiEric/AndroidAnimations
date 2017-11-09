package com.androidanimations.bouncer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.androidanimations.R

/**
 * This example shows simple uses of ValueAnimator, ObjectAnimator, and interpolators
 * to control how a shape is moved around on the screen.
 *
 * The Bouncer1, Bouncer2, and Bouncer3 files are exactly like this one except for variations
 * in how the animation is set up and run.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on YouTube at https://www.youtube.com/watch?v=vCTcmPIKgpM.
 */
class Bouncer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bouncer)
    }

    companion object {
        /**
         * A custom view is used to paint the green background and the shape.
         */
        class MyView : View {

            lateinit var mBitmap : Bitmap
            val paint = Paint()
            var mShapeX: Int = -1
                /**
                 * Moving the shape in x or y causes an invalidation of the area it used to occupy
                 * plus the area it now occupies.
                 */
                set(value) {
                    var minX = mShapeX
                    var maxX = mShapeX + mShapeW
                    field = value
                    minX = Math.min(field, minX)
                    maxX = Math.max(field + mShapeW, maxX)
                    invalidate(minX, mShapeY, maxX, mShapeY + mShapeH)
                }
            var mShapeY = -1
                /**
                 * Moving the shape in x or y causes an invalidation of the area it used to occupy
                 * plus the area it now occupies.
                 */
                set(value) {
                    var minY = mShapeY
                    var maxY = mShapeY + mShapeH
                    field = value
                    minY = Math.min(field, minY)
                    maxY = Math.max(field + mShapeH, maxY)
                    invalidate(mShapeX, minY, mShapeX + mShapeW, maxY)
                }

            var mShapeW = -1
            var mShapeH = -1

            constructor(context: Context): super(context){
                setUpShape()
            }

            constructor(context: Context, attrs: AttributeSet):super(context, attrs){
                setUpShape()
            }

            constructor(context: Context, attrs : AttributeSet, defStyle : Int):
                    super(context,attrs,defStyle){
                setUpShape()
            }

            override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
                mShapeX = (w - mBitmap.width)/2
                mShapeY = 0
            }

            override fun onDraw(canvas: Canvas?) {
                canvas!!.drawBitmap(mBitmap,mShapeX.toFloat(),mShapeY.toFloat(),paint)
            }

            fun startAnimation(){
                // This version uses ValueAnimator, which requires adding an update
                // listener and manually updating the shape position on each frame.
                val anim = getValueAnimator()
                anim.start()
            }

            fun getValueAnimator() : ValueAnimator {
                val anim = ValueAnimator.ofFloat(0f, 1f)
                anim.addUpdateListener { animation ->
                    mShapeY = (animation.animatedFraction * (height - mShapeH)).toInt()
                }
                return anim
            }

            private fun setUpShape() {
                mBitmap = BitmapFactory.decodeResource(resources, R.drawable.electricsheep)
                mShapeW = mBitmap.width
                mShapeH = mBitmap.height
                setOnClickListener { v ->
                    startAnimation()
                }
            }
        }
    }
}
