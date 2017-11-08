package com.androidanimations.windowAnimations

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.androidanimations.R

/**
 * See WindowAnimations.java for comments on the overall application.
 *
 * This is a sub-activity which provides custom animation behavior. When this activity
 * is exited, the user will see the behavior specified in the overridePendingTransition() call.
 */
class AnimatedSubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window_anim_sub)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}
