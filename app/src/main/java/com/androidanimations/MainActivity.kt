package com.androidanimations

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.androidanimations.activityAnimations.ActivityAnimations
import com.androidanimations.anticipation.AnticipationActivity
import com.androidanimations.crossFading.CrossFading
import com.androidanimations.curvedMotion.CurvedMotionActivity
import com.androidanimations.layoutTransChanging.LayoutTransChanging
import com.androidanimations.toonGame.ToonGameActivity
import com.androidanimations.listRemovalAnimation.ListRemovalAnimationActivity
import com.androidanimations.liveButton.LiveButtonActivity
import com.androidanimations.multiPropertyAnimations.MultiPropertyAnimationsActivity
import com.androidanimations.physicsAnimations.ButcherArticleActivity
import com.androidanimations.physicsAnimations.PhysicsActivity
import com.androidanimations.propertyAnimations.PropertyAnimations
import com.androidanimations.squashAndStretch.SquashAndStretchActivity
import com.androidanimations.viewAnimations.ViewAnimations
import com.androidanimations.windowAnimations.WindowAnimations
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private val sDecelerate = DecelerateInterpolator()
  private val sOverShoot = OvershootInterpolator(10f)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun snackbar(view: View){
    val snackbar = Snackbar.make(floatingbutton.rootView, "hello", Snackbar.LENGTH_SHORT)
    snackbar.addCallback(object : Snackbar.Callback(){
      override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
        floatingbutton.animate().setInterpolator(sDecelerate).scaleX(.7f).scaleY(.7f)
                .translationY(transientBottomBar!!.view.height.toFloat()/2).start()
      }

      override fun onShown(sb: Snackbar) {
        super.onShown(sb)
        floatingbutton.animate().setInterpolator(sOverShoot).scaleX(1f)
                .scaleY(1f).translationY(-sb.view.height.toFloat()/2).start()
      }
    }).show()

  }

  fun toonGame(view: View) = StartActivity<ToonGameActivity>()
  fun listView(view: View) = StartActivity<ListRemovalAnimationActivity>()
  fun physics(view: View) = StartActivity<PhysicsActivity>()
  fun butcherPhysics(view: View) = StartActivity<ButcherArticleActivity>()
  fun squashStretch(view: View) = StartActivity<SquashAndStretchActivity>()
  fun multiPropAnim(view: View) = StartActivity<MultiPropertyAnimationsActivity>()
  fun liveButton(view: View) = StartActivity<LiveButtonActivity>()
  fun curvedMotion(view: View) = StartActivity<CurvedMotionActivity>()
  fun anticipation(view: View) = StartActivity<AnticipationActivity>()
  fun activityAnim(view: View) = StartActivity<ActivityAnimations>()
  fun windowAnim(view: View) = StartActivity<WindowAnimations>()
  fun viewAnim(view: View) = StartActivity<ViewAnimations>()
  fun propAnim(view: View) = StartActivity<PropertyAnimations>()
  fun layoutTransChanging(view: View) = StartActivity<LayoutTransChanging>()
  fun crossFade(view: View) = StartActivity<CrossFading>()

  private inline fun <reified T : AppCompatActivity> AppCompatActivity.StartActivity(){
    startActivity(Intent(this, T::class.java))
  }

}