package com.androidanimations.bouncer

import android.animation.ObjectAnimator
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
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import com.androidanimations.R

class Bouncer3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bouncer3)
    }

    companion object {
        class MyView : View {
            lateinit var mBitmap: Bitmap
            private val paint = Paint()
            private var mShapeX = -1
                set(value) {
                    var minX = mShapeX
                    var maxX = mShapeX + mShapeH
                    field = value
                    minX = Math.min(field, minX)
                    maxX = Math.max(field + mShapeW, maxX)
                    invalidate(minX, mShapeY, maxX, mShapeY + mShapeH)
                }
            private var mShapeY = -1
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

            constructor(context: Context) : super(context) {
                setupShape()
            }

            constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
                setupShape()
            }

            constructor(context: Context, attrs: AttributeSet, defStyle: Int) :
                    super(context, attrs, defStyle) {
                setupShape()
            }

            private fun setupShape() {
                mBitmap = BitmapFactory.decodeResource(resources, R.drawable.electricsheep)
                mShapeW = mBitmap.width
                mShapeH = mBitmap.height
                setOnClickListener { v ->
                    startAnimation()
                }
            }

            override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
                super.onSizeChanged(w, h, oldw, oldh)
                mShapeX = (w - mBitmap.width) / 2
                mShapeY = 0
            }

            override fun onDraw(canvas: Canvas?) {
                super.onDraw(canvas)
                canvas!!.drawBitmap(mBitmap, mShapeX.toFloat(), mShapeY.toFloat(), paint)
            }

            fun startAnimation(){
                // This variation uses an ObjectAnimator. The functionality is exactly the same as
                // in Bouncer2, but this time the boilerplate code is greatly reduced because we
                // tell ObjectAnimator to automatically animate the target object for us, so we no
                // longer need to listen for frame updates and do that work ourselves.
                val anim = getObjectAnimator()
                anim.repeatCount = ValueAnimator.INFINITE
                anim.repeatMode = ValueAnimator.REVERSE
                anim.interpolator = AccelerateInterpolator()
                anim.start()
            }

            private fun getObjectAnimator(): ObjectAnimator {
                return ObjectAnimator.ofInt(this, "mShapeY", 0,(height - mShapeH))
            }

        }
    }
}
