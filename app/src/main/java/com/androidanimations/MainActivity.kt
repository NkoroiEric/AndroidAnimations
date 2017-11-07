package com.androidanimations

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.androidanimations.toonGame.ToonGameActivity
import com.androidanimations.listRemovalAnimation.ListRemovalAnimationActivity
import com.androidanimations.multiPropertyAnimations.MultiPropertyAnimationsActivity
import com.androidanimations.physicsAnimations.ButcherArticleActivity
import com.androidanimations.physicsAnimations.PhysicsActivity
import com.androidanimations.squashAndStretch.SquashAndStretchActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    snackbar(listView.rootView)
  }

  fun snackbar(view: View){
    val snackbar = Snackbar.make(floatingbutton.rootView, "hello", Snackbar.LENGTH_SHORT)
    snackbar.addCallback(object : Snackbar.Callback(){
      override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
        floatingbutton.animate().translationY(transientBottomBar!!.view.height.toFloat()/2).start()
      }

      override fun onShown(sb: Snackbar) {
        super.onShown(sb)
        floatingbutton.animate().translationY(-sb.view.height.toFloat()/2).start()
      }
    }).show()

  }

  fun toonGame(view: View){
    StartActivity(ToonGameActivity::class.java)
  }

  fun listView(view: View){
    StartActivity(ListRemovalAnimationActivity::class.java)
  }

  fun physics(view: View){
    StartActivity(PhysicsActivity::class.java)
  }

  fun butcherPhysics(view: View){
    StartActivity(ButcherArticleActivity::class.java)
  }

  fun squashStretch(view: View){
    StartActivity(SquashAndStretchActivity::class.java)
  }

  fun multiPropAnim(view: View){
    StartActivity(MultiPropertyAnimationsActivity::class.java)
  }

  private inline fun <reified T : AppCompatActivity> AppCompatActivity.StartActivity(clazz: Class<T>){
    startActivity(Intent(this, clazz))
  }

}