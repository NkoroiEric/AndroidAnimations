package com.androidanimations.windowAnimations

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.androidanimations.R
import kotlinx.android.synthetic.main.activity_window_animations.*

/**
 * This example shows how to create custom Window animations to animate between different
 * sub-activities.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on YouTube at https://www.youtube.com/watch?v=Ho8vk61lVIU.
 */
class WindowAnimations : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window_animations)

        // By default, launching a sub-activity uses the system default for window animations
        defaultButton.setOnClickListener{v ->
            val subActivity = Intent(this, SubActivity::class.java)
            startActivity(intent)
        }

        // Custom animations allow us to do things like slide the next activity in as we
        // slide this activity out
        translateButton.setOnClickListener { v ->
            // Using the AnimatedSubActivity also allows us to animate exiting that
            // activity - see that activity for details
            val subActivity = Intent(this, AnimatedSubActivity::class.java)
            // The enter/exit animations for the two activities are specified by xml resources
            val translateBundle =
                    ActivityOptions.makeCustomAnimation(this,
                            R.anim.slide_in_left, R.anim.slide_out_left).toBundle()
            startActivity(subActivity, translateBundle)
        }

        // Starting in Jellybean, you can provide an animation that scales up the new
        // activity from a given source rectangle
        scaleButton.setOnClickListener { v ->
            val subActivity = Intent(this, AnimatedSubActivity::class.java)
            val scaleBundle = ActivityOptions.makeScaleUpAnimation(
                    v, 0,0,v.width, v.height
            ).toBundle()
            startActivity(subActivity, scaleBundle)
        }

        thumbnail.setOnClickListener { v ->
            val drawable:BitmapDrawable = thumbnail.drawable as BitmapDrawable
            val bm = drawable.bitmap
            val subActivity = Intent(this, AnimatedSubActivity::class.java)
            val scaleBundle = ActivityOptions.makeThumbnailScaleUpAnimation(
                    thumbnail, bm,0,0
            ).toBundle()
            startActivity(subActivity, scaleBundle)
        }
    }
}
