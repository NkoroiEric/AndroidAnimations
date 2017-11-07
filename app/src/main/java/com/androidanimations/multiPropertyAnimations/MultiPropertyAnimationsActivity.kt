package com.androidanimations.multiPropertyAnimations

import android.animation.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.androidanimations.R

/**
 * This example shows various ways of animating multiple properties in parallel.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on the DevBytes playlist in the androiddevelopers channel on YouTube at
 * https://www.youtube.com/playlist?list=PLWz5rJ2EKKc_XOgcRukSoKKjewFJZrKV0.
 */
class MultiPropertyAnimationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_property_animations)
    }

    /**
     * A very manual approach to animation uses a ValueAnimator to animate a fractional
     * value and then turns that value into the final property values which are then set
     * directly on the target object.
     */
    fun runValueAnimator(view: View){
        val anim = ValueAnimator.ofFloat(0f, 400f)
        anim.addUpdateListener { animator ->
            val fraction = animator.animatedFraction
            view.translationX = TX_START + fraction * (TX_END - TX_START)
            view.translationY = TY_START + fraction * (TY_END - TY_START)
        }
        anim.start()
    }

    /**
     * ViewPropertyAnimator is the cleanest and most efficient way of animating
     * View properties, even when there are multiple properties to be animated
     * in parallel.
     */
    fun runViewPropertyAnimator(view: View){
        view.animate().translationX(TX_END).translationY(TY_END)
    }

    /**
     * Multiple ObjectAnimator objects can be created and run in parallel.
     */
    fun runObjectAnimators(view: View){
        val horizontalAnim = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, TX_END)
        val verticalAnim = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, TY_END)
        val set = AnimatorSet()
        set.playSequentially(verticalAnim,horizontalAnim)
        set.start()
    }

    /**
     * Using PropertyValuesHolder objects enables the use of a single ObjectAnimator
     * per target, even when there are multiple properties being animated on that target.
     */
    fun runObjectAnimator(view: View){
        val pvhTX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, TX_END)
        val pvhTY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, TY_END)
        val objectAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhTX, pvhTY)
        objectAnim.start()
    }

    companion object {
        private val TX_START = 0f
        private val TY_START = 0f
        private val TX_END = 400f
        private val TY_END = 200f
    }
}
