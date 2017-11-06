package com.androidanimations

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.androidanimations.ToonGame.ToonGameActivity
import com.androidanimations.listremovalanimation.ListRemovalAnimationActivity
import com.androidanimations.physicsanimations.ButcherArticleActivity
import com.androidanimations.physicsanimations.PhysicsActivity
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
    startActivity(Intent(this, ToonGameActivity::class.java))
  }

  fun listView(view: View){
    startActivity(Intent(this, ListRemovalAnimationActivity::class.java))
  }

  fun physics(view: View){
    startActivity(Intent(this, PhysicsActivity::class.java))
  }

  fun butcherPhysics(view: View){
    startActivity(Intent(this, ButcherArticleActivity::class.java))
  }

}