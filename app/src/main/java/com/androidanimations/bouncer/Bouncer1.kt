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
 * See the comments in Bouncer for the overall functionality of this app. Changes for this
 * variation are down in the animation setup code.
 */
class Bouncer1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bouncer1)
    }
    companion object {
        class MyView : View {
            lateinit var mBitmap : Bitmap
            val paint = Paint()
            var mShapeX = -1
                set(value) {
                    var minX = mShapeX
                    var maxX = mShapeX + mShapeW
                    field = value
                    minX = Math.min(field, minX)
                    maxX = Math.min(field + mShapeW, maxX)
                    invalidate(minX, mShapeY,maxX,mShapeY + mShapeH)
                }
            var mShapeY = -1
                set(value) {
                    var minY = mShapeY
                    var maxY = mShapeY + mShapeH
                    field = value
                    minY = Math.min(field, minY)
                    maxY = Math.min(field + mShapeH, maxY)
                    invalidate(mShapeX, minY,mShapeX + mShapeW,maxY)
                }
            var mShapeW = -1
            var mShapeH = -1
            constructor(context: Context): super(context){
                setUpShape()
            }
            constructor(context: Context, attrs : AttributeSet): super(context, attrs){
                setUpShape()
            }
            constructor(context: Context, attrs: AttributeSet, defStyle : Int):
                    super(context, attrs,defStyle){
                setUpShape()
            }
            private fun setUpShape() {
                mBitmap = BitmapFactory.decodeResource(resources, R.drawable.electricsheep)
                mShapeW = mBitmap.width
                mShapeH = mBitmap.height
                setOnClickListener { v ->
                    startAnimation()
                }
            }

            override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
                super.onSizeChanged(w, h, oldw, oldh)
                mShapeX = (w - mBitmap.width)/2
                mShapeY = 0
            }

            override fun onDraw(canvas: Canvas?) {
                super.onDraw(canvas)
                canvas!!.drawBitmap(mBitmap, mShapeX.toFloat(), mShapeY.toFloat(), paint)
            }

            fun startAnimation() {
                val anim = getValueAnimator()
                // In this variation, we put the shape into an infinite bounce, where it keeps moving
                // up and down. Note that it's still not actually "bouncing" because it just uses
                // default time interpolation.
                anim.repeatCount = ValueAnimator.INFINITE
                anim.repeatMode = ValueAnimator.REVERSE
                anim.start()
            }

            fun getValueAnimator():ValueAnimator{
                val anim = ValueAnimator.ofFloat(0f,1f)
                anim.addUpdateListener { animation ->
                    mShapeY = (animation.animatedFraction * (height - mShapeH)).toInt()
                }
                return anim
            }
        }
    }
}
