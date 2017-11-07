package com.androidanimations.toonGame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

/**
 * Created by scaleup on 06/11/17.
 */
class SkewableTextView : TextView{

    var mSkewX : Float = 0.0f
        get() = field
        set(value) {
            if (value != mSkewX){
                field = value
                invalidate()//force redraw with new value
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
           mTempRect.set(0f,0f, right.toFloat(), bottom.toFloat())
           matrix.mapRect(mTempRect)
           mTempRect.offset(left.toFloat() + translationX, top + translationY)
           (parent as View).invalidate(mTempRect.left.toInt(), mTempRect.top.toInt(),
                   (mTempRect.right + .5f).toInt(), (mTempRect.bottom + .5f).toInt())
       }
    }

    val mTempRect = RectF()
    constructor(context: Context):super(context){

    }

    constructor(context: Context, attrs : AttributeSet): super(context, attrs){

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle){

    }

    override fun onDraw(canvas: Canvas?) {
        if (mSkewX != 0f){
            canvas!!.translate(0f, height.toFloat())
            canvas.skew(mSkewX, 0f)
            canvas.translate(0f, -height.toFloat())
        }
        super.onDraw(canvas)
    }


}