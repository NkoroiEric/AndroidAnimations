package com.androidanimations.viewAnimations

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.CheckBox
import com.androidanimations.R
import kotlinx.android.synthetic.main.activity_view_animations.*

/**
 * This example shows how to use pre-3.0 view Animation classes to create various animated UI
 * effects. See also the demo PropertyAnimations, which shows how this is done using the new
 * ObjectAnimator API introduced in Android 3.0.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on YouTube at https://www.youtube.com/watch?v=_UWXqFBF86U.
 */
class ViewAnimations : AppCompatActivity() {

    lateinit var mCheckbox : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animations)
        mCheckbox = checkbox


        //Fade the button out and back in
        val alphaAnimation = AlphaAnimation(1f,0f)
        alphaAnimation.duration = 1000

        //move the button over and then back
        val translateAnimation = TranslateAnimation(Animation.ABSOLUTE, 0f,
                Animation.RELATIVE_TO_PARENT, 1f,
                Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 100f)

        //spin the button around in a full circle
        val rotateAnim = RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,
                .5f,Animation.RELATIVE_TO_SELF, .5f)
        rotateAnim.duration = 1000

        //scale the button in x and y
        val scaleAnimation = ScaleAnimation(1f,2f,1f,2f)
        scaleAnimation.duration = 1000

        //run the animations above in sequence on the final button. Looks horrible
        val set = AnimationSet(true)
        set.addAnimation(alphaAnimation)
        set.addAnimation(translateAnimation)
        set.addAnimation(rotateAnim)
        set.addAnimation(scaleAnimation)

        setupAnimation(alphaButton, alphaAnimation, R.anim.alpha_anim)
        setupAnimation(translateButton, translateAnimation, R.anim.translate_anim)
        setupAnimation(rotateButton, rotateAnim, R.anim.rotate_anim)
        setupAnimation(scaleButton, scaleAnimation, R.anim.scale_anim)
        setupAnimation(setButton, set, R.anim.set_anim)
    }

    private fun setupAnimation(view: View, animation: Animation, animId: Int) {
        view.setOnClickListener { v ->
            // If the button is checked, load the animation from the given resource
            // id instead of using the passed-in animation paramter. See the xml files
            // for the details on those animations.
            v.startAnimation(
                    if (mCheckbox.isChecked)
                        AnimationUtils.loadAnimation(this, animId)
                    else
                        animation
            )
        }
    }
}
