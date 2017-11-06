package com.androidanimations.listremovalanimation

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.androidanimations.R


/**
 * Created by scaleup on 05/11/17.
 */
class BackgroundContainer : FrameLayout {

    var mshowing = false
    lateinit var mShadowedBackground:Drawable
    var mOpenAreaTop = 0
    var mOpenAreaBottom = 0
    var mOpenAreaHeight = 0
    var mUpdateBounds = false


    constructor(context: Context) : super(context){
        init(context)
    }



    constructor(context: Context, attr : AttributeSet):super(context, attr){
        init(context)
    }

    constructor(context: Context, attr: AttributeSet, defStyle: Int):super(context, attr, defStyle){
        init(context)
    }

    private fun init(context: Context) {
        mShadowedBackground =
                context.resources.getDrawable(R.drawable.shadowed_background)
    }

    public fun showBackground(top: Int, bottom : Int){
        setWillNotDraw(false)
        mOpenAreaTop = top
        mOpenAreaHeight = bottom
        mshowing = true
        mUpdateBounds = true
    }

    public fun hideBackground(){
        setWillNotDraw(true)
        mshowing = false
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mshowing){
            if (mUpdateBounds){
                mShadowedBackground.setBounds(0,0,width, mOpenAreaHeight)
            }
            canvas!!.save()
            canvas.translate(0f, mOpenAreaTop.toFloat())
            mShadowedBackground.draw(canvas)
            canvas.restore()

        }
    }
}
