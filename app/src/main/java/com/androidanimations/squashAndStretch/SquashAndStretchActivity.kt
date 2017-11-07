package com.androidanimations.squashAndStretch

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.androidanimations.R
import android.view.animation.DecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.ViewGroup
import kotlinx.android.synthetic.main.squash_and_stretch.*


/**
 * This example shows how to add some life to a view during animation by deforming the shape.
 * As the button "falls", it stretches along the line of travel. When it hits the bottom, it
 * squashes, like a real object when hitting a surface. Then the button reverses these actions
 * to bounce back up to the start.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on the DevBytes playlist in the androiddevelopers channel on YouTube at
 * https://www.youtube.com/playlist?list=PLWz5rJ2EKKc_XOgcRukSoKKjewFJZrKV0.
 */
class SquashAndStretchActivity : AppCompatActivity() {

    private lateinit var mContainer: ViewGroup
    private val BASE_DURATION: Long = 300
    private var sAnimatorScale: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.squash_and_stretch)
        mContainer = container
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.squash,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.menu_slow){
            sAnimatorScale = if (item.isChecked) 1 else 5
            item.isChecked = !item.isChecked
        }
        return super.onOptionsItemSelected(item)
    }

    fun onButtonClick(view: View){
        val animationDuration: Long = BASE_DURATION * sAnimatorScale

        //scale around bottom/middle to simplify squash aganist the window bottom
        view.pivotY = view.height.toFloat()
        view.pivotX = (view.width/2).toFloat()

        //Animate the bottom down, accelerating, while also stretching in Y and squashing in X
        var pvhTY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, (mContainer.height - view.height).toFloat())
        var pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, .7f)
        var pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f)
        val downAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhTY, pvhSX, pvhSY)
        downAnim.interpolator = sAccelerator
        downAnim.duration = animationDuration * 2


        //Stretching in X, Squash in Y then reverse
        pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
        pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 5f)
        val stretchAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhSX, pvhSY)
        stretchAnim.repeatCount = 1
        stretchAnim.repeatMode = ValueAnimator.REVERSE
        stretchAnim.interpolator = sDecelerator
        stretchAnim.duration = animationDuration

        //Animate back to the start
        pvhTY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f)
        pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
        val upAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhTY , pvhSX , pvhSY)
        upAnim.duration = animationDuration * 2
        upAnim.interpolator = sDecelerator

        val set = AnimatorSet()
        set.playSequentially(downAnim, stretchAnim, upAnim)
        set.start()
    }

    companion object {

        private val sAccelerator = AccelerateInterpolator()
        private val sDecelerator = DecelerateInterpolator()
    }
}
