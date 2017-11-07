package com.androidanimations.listRemovalAnimation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ListView
import com.androidanimations.R
import kotlinx.android.synthetic.main.fragment_list_removal_animation.*


class ListRemovalAnimationActivity : AppCompatActivity() {
    lateinit var mAdapter: CheeseArrayAdapter
    lateinit var mListView: ListView
    lateinit var mBackgroundContainer: BackgroundContainer
    var mSwiping = false
    var mItemPressed = false
    var mItemIdTopMap = HashMap<Long, Int>()

    private val SWIPE_DURATION = 250
    private val MOVE_DURATION = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list_removal_animation)
        mBackgroundContainer = listViewBackground
        mListView = listview
        //android.util.Log.d("Debug", "d=" + mListView.divider!!)
        val cheeseList = ArrayList<String>()
        for (i in Cheeses.sCheeseStrings.indices) {
            cheeseList.add(Cheeses.sCheeseStrings[i])
        }
        mAdapter = CheeseArrayAdapter(this, R.layout.opaque_text_view, cheeseList,
                mTouchListener)
        mListView.adapter = mAdapter
    }

    /**
     * Handle touch events to fade/move dragged items as they are swiped out
     */
    private val mTouchListener = object : View.OnTouchListener {

        internal var mDownX: Float = 0.toFloat()
        private var mSwipeSlop = -1

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(this@ListRemovalAnimationActivity).scaledTouchSlop
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return true
                    }
                    mItemPressed = true
                    mDownX = event.x
                }
                MotionEvent.ACTION_CANCEL -> {
                    v.setAlpha(1f)
                    v.setTranslationX(0f)
                    mItemPressed = false
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = event.x + v.translationX
                    val deltaX = x - mDownX
                    val deltaXAbs = Math.abs(deltaX)
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true
                            mListView.requestDisallowInterceptTouchEvent(true)
                            mBackgroundContainer.showBackground(v.top, v.height)
                        }
                    }
                    if (mSwiping) {
                        v.translationX = x - mDownX
                        v.alpha = 1 - deltaXAbs / v.width
                    }
                }
                MotionEvent.ACTION_UP -> {
                    run {
                        // User let go - figure out whether to animate the view out, or back into place
                        if (mSwiping) {
                            val x = event.x + v.translationX
                            val deltaX = x - mDownX
                            val deltaXAbs = Math.abs(deltaX)
                            val fractionCovered: Float
                            val endX: Float
                            val endAlpha: Float
                            val remove: Boolean
                            if (deltaXAbs > v.width / 4) {
                                // Greater than a quarter of the width - animate it out
                                fractionCovered = deltaXAbs / v.width
                                endX = if (deltaX < 0) -v.width.toFloat() else v.width.toFloat()
                                endAlpha = 0f
                                remove = true
                            } else {
                                // Not far enough - animate it back
                                fractionCovered = 1 - deltaXAbs / v.width
                                endX = 0f
                                endAlpha = 1f
                                remove = false
                            }
                            // Animate position and alpha of swiped item
                            // NOTE: This is a simplified version of swipe behavior, for the
                            // purposes of this demo about animation. A real version should use
                            // velocity (via the VelocityTracker class) to send the item off or
                            // back at an appropriate speed.
                            val duration = ((1 - fractionCovered) * SWIPE_DURATION).toInt().toLong()
                            mListView.isEnabled = false
                            v.animate().setDuration(duration).alpha(endAlpha).translationX(endX).withEndAction {
                                // Restore animated values
                                v.setAlpha(1f)
                                v.setTranslationX(0f)
                                if (remove) {
                                    animateRemoval(mListView, v)
                                } else {
                                    mBackgroundContainer.hideBackground()
                                    mSwiping = false
                                    mListView.isEnabled = true
                                }
                            }
                        }
                    }
                    mItemPressed = false
                }
                else -> return false
            }
            return true
        }
    }

    /**
     * This method animates all other views in the ListView container (not including ignoreView)
     * into their final positions. It is called after ignoreView has been removed from the
     * adapter, but before layout has been run. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private fun animateRemoval(listview: ListView, viewToRemove: View) {
        val firstVisiblePosition = listview.firstVisiblePosition
        for (i in 0 until listview.childCount) {
            val child = listview.getChildAt(i)
            if (child !== viewToRemove) {
                val position = firstVisiblePosition + i
                val itemId = mAdapter.getItemId(position)
                mItemIdTopMap.put(itemId, child.top)
            }
        }
        // Delete the item from the adapter
        val position = mListView.getPositionForView(viewToRemove)
        mAdapter.remove(mAdapter.getItem(position))

        val observer = listview.viewTreeObserver
        observer.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                observer.removeOnPreDrawListener(this)
                var firstAnimation = true
                val firstVisiblePosition1 = listview.firstVisiblePosition
                for (i in 0 until listview.childCount) {
                    val child = listview.getChildAt(i)
                    val position1 = firstVisiblePosition1 + i
                    val itemId = mAdapter.getItemId(position1)
                    var startTop = mItemIdTopMap[itemId]
                    val top = child.top
                    if (startTop != null) {
                        if (startTop != top) {
                            val delta = startTop - top
                            child.setTranslationY(delta.toFloat())
                            child.animate().setDuration(MOVE_DURATION.toLong()).translationY(0f)
                            if (firstAnimation) {
                                child.animate().withEndAction {
                                    mBackgroundContainer.hideBackground()
                                    mSwiping = false
                                    mListView.isEnabled = true
                                }
                                firstAnimation = false
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        val childHeight = child.height + listview.dividerHeight
                        startTop = top + if (i > 0) childHeight else -childHeight
                        val delta = startTop - top
                        child.translationY = delta.toFloat()
                        child.animate().setDuration(MOVE_DURATION.toLong()).translationY(0f)
                        if (firstAnimation) {
                            child.animate().withEndAction {
                                mBackgroundContainer.hideBackground()
                                mSwiping = false
                                mListView.isEnabled = true
                            }
                            firstAnimation = false
                        }
                    }
                }
                mItemIdTopMap.clear()
                return true
            }
        })
    }
}
