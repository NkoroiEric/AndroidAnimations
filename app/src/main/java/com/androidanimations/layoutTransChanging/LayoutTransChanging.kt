package com.androidanimations.layoutTransChanging

import android.animation.LayoutTransition
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.androidanimations.R
import kotlinx.android.synthetic.main.activity_layout_trans_changing.*
import android.view.ViewGroup



/**
 * This example shows how to use LayoutTransition to animate simple changes in a layout
 * container.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on YouTube at https://www.youtube.com/watch?v=55wLsaWpQ4g.
 */
class LayoutTransChanging : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_trans_changing)

        //start with 2 views
        container.addView(ColoredView(this))
        container.addView(ColoredView(this))

        addButton.setOnClickListener { v ->
            //Adding a view will cause a LayoutTransition animation
            container.addView(ColoredView(this))
        }

        removeButton.setOnClickListener{v ->
            //removing a view will cause a LayoutTransition animation
            container.removeViewAt(Math.min(1, container.childCount -1))
        }

        // Note that this assumes a LayoutTransition is set on the container, which is the
        // case here because the container has the attribute "animateLayoutChanges" set to true
        // in the layout file. You can also call setLayoutTransition(new LayoutTransition()) in
        // code to set a LayoutTransition on any container.
        val transition = container.layoutTransition

        // New capability as of Jellybean; monitor the container for *all* layout changes
        // (not just add/remove/visibility changes) and animate these changes as well.
        transition.enableTransitionType(LayoutTransition.CHANGING)

    }

    /**
     * Custom view painted with a random background color and two different sizes which are
     * toggled between due to user interaction.
     */
    class ColoredView(context: Context) : View(context) {

        private var mExpanded = false

        private val mCompressedParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 50)

        private val mExpandedParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 200)

        init {
            val  red = (Math.random() * 128 + 127).toInt()
            val  green= (Math.random() * 128 + 127).toInt()
            val  blue = (Math.random() * 128 + 127).toInt()
            val color = 0xff shl 24 or (red shl 16) or
                    (green shl 8) or blue
            setBackgroundColor(color)
            layoutParams = mCompressedParams
            setOnClickListener { v ->
                // Size changes will cause a LayoutTransition animation if the CHANGING
                // transition is enabled
                layoutParams = if (mExpanded) mCompressedParams else mExpandedParams
                mExpanded = !mExpanded
                requestLayout()
            }
        }
    }

}

