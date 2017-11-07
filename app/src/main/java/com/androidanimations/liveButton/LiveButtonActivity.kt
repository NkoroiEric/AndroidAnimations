package com.androidanimations.liveButton

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.androidanimations.R
import android.view.animation.OvershootInterpolator
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.activity_live_button.*



/**
 * This app shows a simple application of anticipation and follow-through techniques as
 * the button animates into its pressed state and animates back out of it, overshooting
 * end state before resolving.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on the DevBytes playlist in the androiddevelopers channel on YouTube at
 * https://www.youtube.com/playlist?list=PLWz5rJ2EKKc_XOgcRukSoKKjewFJZrKV0.
 */
class LiveButtonActivity : AppCompatActivity() {

    var sDecelerator = DecelerateInterpolator(4f)
    var sOvershooter = OvershootInterpolator(10f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_button)

        val clickMeButton = clickMe
        clickMeButton.animate().duration = 200

        clickMeButton.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                clickMeButton.animate().setInterpolator(sDecelerator)
                        .scaleX(.7f).scaleY(.7f)
            }else if (event.action == MotionEvent.ACTION_UP){
                clickMeButton.animate().setInterpolator(sOvershooter)
                        .scaleX(1f).scaleY(1f)
            }
            false
        }
    }
}
