package com.androidanimations.propertyAnimations

import android.animation.*
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.ViewAnimator
import com.androidanimations.R
import kotlinx.android.synthetic.main.activity_property_animations.*

class PropertyAnimations : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_animations)

        //Fade the button out and back in
        val alphaAnimation = ObjectAnimator.ofFloat(alphaButton, View.ALPHA, 0f)
        alphaAnimation.repeatCount = 1
        alphaAnimation.repeatMode = ObjectAnimator.REVERSE

        //move the button over to the right and then back
        val translateAnimation = ObjectAnimator.ofFloat(translateButton, View.TRANSLATION_X, 800f)
        translateAnimation.repeatCount = 1
        translateAnimation.repeatMode = ValueAnimator.REVERSE

        //spin the button around in a full circle
        val rotateAnimation = ObjectAnimator.ofFloat(rotateButton, View.ROTATION, 360f)
        rotateAnimation.repeatCount = 1
        rotateAnimation.repeatMode = ValueAnimator.REVERSE

        //Scale the button in X and Y. Note the use of propertyValuesHolder to animate
        //multiple properties on the same object in parallel
        val pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
        val pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2f)
        val scaleAnimation = ObjectAnimator.ofPropertyValuesHolder(scaleButton, pvhX, pvhY)
        scaleAnimation.repeatCount = 1
        scaleAnimation.repeatMode = ValueAnimator.REVERSE

        //Run the animation above in sequence
        val setAnimation = AnimatorSet()
        setAnimation.play(translateAnimation).after(alphaAnimation).before(rotateAnimation)
        setAnimation.play(rotateAnimation).before(scaleAnimation)


        setupAnimation(alphaButton, alphaAnimation, R.animator.fade);
        setupAnimation(translateButton, translateAnimation, R.animator.move);
        setupAnimation(rotateButton, rotateAnimation, R.animator.spin);
        setupAnimation(scaleButton, scaleAnimation, R.animator.scale);
        setupAnimation(setButton, setAnimation, R.animator.combo)

    }

    private fun setupAnimation(view: View, animation: Animator, animationId: Int) {
        view.setOnClickListener { v ->
            // If the button is checked, load the animation from the given resource
            // id instead of using the passed-in animation parameter. See the xml files
            // for the details on those animations.
            if (checkbox.isChecked){
                val anim = AnimatorInflater.loadAnimator(this, animationId)
                anim.setTarget(v)
                anim.start();
                return@setOnClickListener;
            }
            animation.start()
        }
    }
}
