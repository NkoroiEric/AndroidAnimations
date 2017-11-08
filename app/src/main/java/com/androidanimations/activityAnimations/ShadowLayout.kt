package com.androidanimations.activityAnimations

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.BlurMaskFilter.Blur


/**
 * Created by scaleup on 07/11/17.
 */

/**
 * This custom layout paints a drop shadow behind all children. The size and opacity
 * of the drop shadow is determined by a "depth" factor that can be set and animated.
 */
class ShadowLayout : RelativeLayout{

    var mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mShadowDepth: Float = 0f
    lateinit var mShadowBitmap: Bitmap

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attr : AttributeSet): super(context, attr){
        init()
    }

    constructor(context: Context, attr: AttributeSet, defStyle : Int): super(context, attr, defStyle){
        init()
    }

    /**
     * Called by the constructors - sets up the drawing parameters for the drop shadow.
     */
    private fun init() {
        mShadowPaint.color = Color.BLACK
        mShadowPaint.style = Paint.Style.FILL
        setWillNotDraw(false)
        mShadowBitmap = Bitmap.createBitmap(sShadowRect.width(),
                sShadowRect.height(), Bitmap.Config.ARGB_8888)
        val c = Canvas(mShadowBitmap)
        mShadowPaint.maskFilter = BlurMaskFilter(BLUR_RADIUS.toFloat(), Blur.NORMAL)
        c.translate(BLUR_RADIUS.toFloat(), BLUR_RADIUS.toFloat())
        c.drawRoundRect(sShadowRectF, sShadowRectF.width() / 40,
                sShadowRectF.height() / 40, mShadowPaint)
    }

    companion object {
        val BLUR_RADIUS = 6
        val sShadowRectF = RectF(0f, 0f, 200f, 200f)
        val sShadowRect = Rect(0, 0, 200 + 2 * BLUR_RADIUS, 200 + 2 * BLUR_RADIUS)
        var tempShadowRectF = RectF(0f, 0f, 0f, 0f)
    }
}