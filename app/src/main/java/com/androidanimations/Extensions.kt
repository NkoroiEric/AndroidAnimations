package com.androidanimations


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * Created by scaleup on 04/11/17.
 */

inline fun  FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction){
    beginTransaction().func().commit()
}

fun FragmentManager.addFragment(id: Int, t: Fragment){
   inTransaction { add(id, t) }
}

fun FragmentManager.replaceFragment(id: Int, t: Fragment){
    inTransaction { replace(id, t) }
}