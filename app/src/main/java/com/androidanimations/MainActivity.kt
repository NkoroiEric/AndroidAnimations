package com.androidanimations

import android.os.Bundle
import android.support.animation.*
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.StringBuilderPrinter
import android.view.View
import android.widget.ImageView
import com.androidanimations.listremovalanimation.ListRemovalAnimationFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragments_container.*


class MainActivity : AppCompatActivity() {

  private lateinit var flingAnimation: FlingAnimation
  private lateinit var springAnimation: SpringAnimation
  private lateinit var scale : FloatPropertyCompat<View>
//  private lateinit var floatbutton : FloatingActionButton
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragments_container)
    supportFragmentManager.addFragment(R.id.container,ListRemovalAnimationFragment.newInstance("hello","world"))

    scale = object : FloatPropertyCompat<View>("scale"){
      override fun getValue(`object`: View): Float {
        // return the value of any one property
        return `object`.scaleX
      }

      override fun setValue(`object`: View, value: Float) {
        // Apply the same value to two properties
        `object`.scaleX = value
        `object`.scaleY = value
      }
    }

  }

  fun snackbar(view: View){
    val snackbar = Snackbar.make(this.floatbutton.rootView, "hello", Snackbar.LENGTH_SHORT)
    snackbar.addCallback(object : Snackbar.Callback(){
      override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
        floatbutton.animate().translationY(transientBottomBar!!.view.height.toFloat()/2).start()
      }

      override fun onShown(sb: Snackbar) {
        super.onShown(sb)
        floatbutton.animate().translationY(-sb.view.height.toFloat()/2).start()
      }
    }).show()

  }

  fun flingIt(view : View){
    flingAnimation = FlingAnimation(emoji, DynamicAnimation.X)
    flingAnimation.setStartVelocity(500f)
    flingAnimation.friction = 0.5f
    flingAnimation.start()
  }


  fun bounce(view: View){
    emoji.setImageResource(R.drawable.ic_sentiment_neutral_black_24dp)
    springAnimation = SpringAnimation(emoji, DynamicAnimation.X)
    val springForce = SpringForce()
    springForce.finalPosition = emoji.x
    springForce.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
    springForce.stiffness = SpringForce.STIFFNESS_LOW
    springAnimation.spring = springForce
    springAnimation.setStartVelocity(-200f)
    springAnimation.start()
    springAnimation.addEndListener { animation, canceled, value, velocity -> emoji.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_56dp) }
  }

  private lateinit var stretchAnimation: SpringAnimation

  fun stretch(view: View){
    stretchAnimation = SpringAnimation(emoji, scale)
    stretchAnimation.minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_SCALE
    val springForce = SpringForce()
    springForce.finalPosition = emoji.x
    springForce.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
    springForce.stiffness = SpringForce.STIFFNESS_VERY_LOW
    stretchAnimation.spring = springForce
    stretchAnimation.setStartVelocity(2000f)
    stretchAnimation.start()
  }

  private lateinit var transition : SpringAnimation
  fun transition(view: View){
    transition = SpringAnimation(emoji, SpringAnimation.TRANSLATION_Y)
    val springForce = SpringForce()
    springForce.finalPosition = 0f
    springForce.stiffness = SpringForce.STIFFNESS_VERY_LOW
    springForce.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
    transition.spring = springForce
    transition.setStartVelocity(4000f)
    transition.start()
  }
}
