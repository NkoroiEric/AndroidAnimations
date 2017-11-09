package com.androidanimations.crossFading

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.androidanimations.BuildConfig
import com.androidanimations.R
import kotlinx.android.synthetic.main.activity_cross_fading.*

/**
 * This example shows how to use TransitionDrawable to perform a simple cross-fade effect
 * between two drawables.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on YouTube at https://www.youtube.com/watch?v=atH3o2uh_94.
 */
class CrossFading : AppCompatActivity() {

    var mCurrentDrawable : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_fading)

//        val bitmap0 = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
//        val bitmap1 = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
//        var canvas = Canvas(bitmap0)
//        canvas.drawColor(Color.RED)
//        canvas = Canvas(bitmap1)
//        canvas.drawColor(Color.GREEN)
        val drawables:ArrayList<BitmapDrawable> = ArrayList()
//        drawables[0] = BitmapDrawable(resources, bitmap0)
//        drawables[1] = BitmapDrawable(resources, bitmap1)

        for (i in 0 until  20){
            val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(color())
            drawables.add(BitmapDrawable(resources, bitmap))
        }
        // Add the red/green bitmap drawables to a TransitionDrawable. They are layered
        // in the transition drawalbe. The cross-fade effect happens by fading one out and the
        // other in.
        Log.w(BuildConfig.APPLICATION_ID, drawables.toString())
        val crossFader = TransitionDrawable(drawables.toArray(arrayOfNulls<BitmapDrawable>(20)))
        imageview.setImageDrawable(crossFader)

        // Clicking on the drawable will cause the cross-fade effect to run. Depending on
        // which drawable is currently being shown, we either 'start' or 'reverse' the
        // transition, which determines which drawable is faded out/in during the transition.
        imageview.setOnClickListener { v ->
            mCurrentDrawable++
            if (mCurrentDrawable % 2 == 0){
                 crossFader.startTransition(500)
//                mCurrentDrawable = 1
            }else {
                crossFader.reverseTransition(500)
//                mCurrentDrawable = 0
            }
        }

    }

    fun color(): Int{
        val  red = (Math.random() * 128 + 127).toInt()
        val  green= (Math.random() * 128 + 127).toInt()
        val  blue = (Math.random() * 128 + 127).toInt()
        val color = 0xff shl 24 or (red shl 16) or
                (green shl 8) or blue
        return color
    }
}
