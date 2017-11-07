package com.androidanimations.toonGame

import android.animation.*
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import com.androidanimations.R


class PlayerSetupActivity : AppCompatActivity() {

    lateinit var mContainer: ViewGroup
    lateinit var mEditText: EditText
    lateinit var mNameTV: SkewableTextView
    lateinit var mDifficultyTV:SkewableTextView
    lateinit var mCreditTV:SkewableTextView

    lateinit var mNameButtons: ViewGroup
    lateinit var mDifficultyButtons:ViewGroup
    lateinit var mCreditButtons1:ViewGroup
    lateinit var mCreditButtons2:ViewGroup

    lateinit var mBobButton: Button
    lateinit var mJaneButton:Button
    lateinit var mPatButton:Button

    var mEntryState = NAME_STATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_setup_layout)
        overridePendingTransition(0,0)

        mContainer = findViewById(R.id.container)
        mContainer.viewTreeObserver.addOnPreDrawListener(mPreDrawListener)

        mNameTV = findViewById(R.id.nameTV)
        mDifficultyTV = findViewById(R.id.ageTV)
        mCreditTV = findViewById(R.id.creditTV)

        mBobButton = setupButton(R.id.bobButton)
        setupButton(R.id.janeButton);
        setupButton(R.id.patButton);
        setupButton(R.id.easyButton);
        setupButton(R.id.hardButton);
        setupButton(R.id.megaHardButton);

        mNameButtons = findViewById(R.id.nameButtons)
        mDifficultyButtons = findViewById(R.id.difficultyButtons)
        mCreditButtons1 = findViewById(R.id.creditButtons1)
        mCreditButtons2 = findViewById(R.id.creditButtons2)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,0)
    }

    private fun setupButton(resourceId: Int): Button {
        val button = findViewById<Button>(resourceId)
        button.setOnTouchListener(mButtonPressListener)
        return button
    }

    private val mButtonPressListener = object  : View.OnTouchListener{
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    v.animate().setDuration(ToonGameActivity.SHORT_DURATION)
                            .scaleX(.8f).scaleY(.8f).interpolator = sDecelerator
                }

                MotionEvent.ACTION_UP -> {
                    v.animate().setDuration(ToonGameActivity.SHORT_DURATION)
                            .scaleX(1f).scaleY(1f).interpolator = sAccelerator
                }
            }
            return false
        }

    }

    fun buttonClick(clickedView : View , alignmentRule : Int){
        val parent:ViewGroup = clickedView.parent as ViewGroup
        for (i in 0 until parent.childCount){
            val child = parent.getChildAt(i) as Button
            if (child != clickedView){
                child.animate().alpha(0f)
            }else{
                val buttonCopy = Button(this)
                child.visibility = View.INVISIBLE
                buttonCopy.background = child.background
                buttonCopy.text = child.text.toString()
                val params = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                params.addRule(alignmentRule)
                params.setMargins(25,50,25,50)
                buttonCopy.layoutParams = params
                buttonCopy.setPadding(child.paddingLeft, child.paddingTop, child.paddingRight, child.paddingBottom)
                buttonCopy.setTextSize(TypedValue.COMPLEX_UNIT_PX, child.paddingTop.toFloat())
                buttonCopy.setTypeface(child.getTypeface(), Typeface.BOLD);
                val colors = child.textColors
                buttonCopy.setTextColor(colors.defaultColor)
                val oldLocationInWindow = IntArray(2)
                child.getLocationInWindow(oldLocationInWindow)
                mContainer.addView(buttonCopy)
                buttonCopy.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
                    override fun onPreDraw(): Boolean {
                        buttonCopy.viewTreeObserver.removeOnPreDrawListener(this)
                        val locationInWindow = IntArray(2)
                        val deltaX = oldLocationInWindow[0] - locationInWindow[0]
                        val deltaY = oldLocationInWindow[1] - locationInWindow[1]

                        buttonCopy.translationX = deltaX.toFloat()
                        buttonCopy.translationY = deltaY.toFloat()

                        val pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, 3f)
                        val pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 3f)
                        val bounceAnim = ObjectAnimator.ofPropertyValuesHolder(buttonCopy, pvhSX, pvhSY)
                        bounceAnim.repeatCount = 1
                        bounceAnim.repeatMode = ValueAnimator.REVERSE
                        bounceAnim.interpolator = sDecelerator
                        bounceAnim.duration = 300

                        val pvhTX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f)
                        val pvhTY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f)
                        val moveAnim = ObjectAnimator.ofPropertyValuesHolder(buttonCopy, pvhTX, pvhTY)
                        moveAnim.duration = 600
                        bounceAnim.start()
                        moveAnim.start()
                        moveAnim.addListener(object : AnimatorListenerAdapter(){
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                when (mEntryState){
                                    NAME_STATE -> {
                                        val runnable = Runnable {
                                            mDifficultyButtons.visibility = View.VISIBLE
                                            mNameButtons.visibility = View.GONE
                                            popChildrenIn(mDifficultyButtons , null)
                                        }
                                        slideToNext(mNameTV, mDifficultyTV, runnable)
                                        mEntryState = DIFFICULTY_STATE
                                    }

                                    DIFFICULTY_STATE -> {
                                        mDifficultyButtons.visibility = View.GONE
                                        for (i in 0 until 5){
                                            mCreditButtons1.addView(setupNumberButton(i))
                                        }
                                        for (i in 0 until 10){
                                            mCreditButtons2.addView(setupNumberButton(i))
                                        }
                                        val runnable = Runnable {
                                            mCreditButtons1.visibility = View.VISIBLE
                                            val runnable = Runnable {
                                                mCreditButtons2.visibility = View.VISIBLE
                                                popChildrenIn(mCreditButtons2, null)
                                            }
                                            popChildrenIn(mCreditButtons1, runnable)
                                        }
                                        slideToNext(mDifficultyTV, mCreditTV, runnable)
                                        mEntryState = CREDIT_STATE
                                    }
                                }
                            }
                        })
                        return true
                    }

                })

            }
        }
    }

    fun selectDifficulty(clickedView: View){
        buttonClick(clickedView, RelativeLayout.ALIGN_PARENT_RIGHT)
    }

    fun selectName(clickedView: View){
        buttonClick(clickedView, RelativeLayout.ALIGN_PARENT_LEFT)
    }

    fun setupNumberButton(number : Int): Button{
        val button = Button(this)
        button.textSize = 15f
        button.setTextColor(Color.WHITE)
        button.setTypeface(mBobButton.typeface, Typeface.BOLD)
        button.text = number.toString()
        button.setPadding(0,0,0,0)

        val oval = OvalShape()
        val drawable = ShapeDrawable(oval)
        drawable.paint.color = 0xFF shl 24 or ((50 + 150 * Math.random()).toInt() shl 16) or (
                (50 + 150 * Math.random()).toInt() shl 8) or (50 + 150 * Math.random()).toInt()
        button.background = drawable
        button.setOnTouchListener(mButtonPressListener)
        return button
    }

    var mPreDrawListener: ViewTreeObserver.OnPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {

        override fun onPreDraw(): Boolean {
            mContainer.viewTreeObserver.removeOnPreDrawListener(this)
            mContainer.setScaleX(0f)
            mContainer.setScaleY(0f)
            mContainer.animate().scaleX(1f).scaleY(1f).interpolator = OvershootInterpolator()
            mContainer.animate().setDuration(ToonGameActivity.LONG_DURATION).withEndAction {
                val buttonsParent = findViewById<View>(R.id.nameButtons) as ViewGroup
                buttonsParent.visibility = View.VISIBLE
                popChildrenIn(buttonsParent, null)
            }
            return false
        }
    }

    private fun popChildrenIn(parent : ViewGroup, endAction: Runnable?) {
        // for all children, scale in one at a time
        val overshooter = OvershootInterpolator()
        val childCount = parent.childCount
        val childAnims = arrayOfNulls<ObjectAnimator>(childCount)
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            child.scaleX = 0f
            child.scaleY = 0f
            val pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
            val pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
            val anim = ObjectAnimator.ofPropertyValuesHolder(child, pvhSX, pvhSY)
            anim.duration = 150
            anim.interpolator = overshooter
            childAnims[i] = anim
        }
        val set = AnimatorSet()
        set.playSequentially(*childAnims)
        set.start()
        if (endAction != null) {
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    endAction.run()
                }
            })
        }
    }

    private fun slideToNext(currentView :SkewableTextView ,nextView : SkewableTextView, endAction : Runnable?) {
          // skew/anticipate current view, slide off, set GONE, restore translation
        val currentSkewer = ObjectAnimator.ofFloat(currentView, "skewX", -.5f);
        currentSkewer.setInterpolator(sDecelerator);
        val currentMover = ObjectAnimator.ofFloat(currentView, View.TRANSLATION_X,
                -mContainer.getWidth().toFloat());
        currentMover.setInterpolator(sLinearInterpolator);
        currentMover.setDuration(ToonGameActivity.MEDIUM_DURATION);

        // set next view visible, translate off to right, skew,
        // slide on in parallel, overshoot/wobble, unskew
        nextView.setVisibility(View.VISIBLE);
        nextView.mSkewX = -.5f
        nextView.translationX = mContainer.getWidth().toFloat();


        val nextMover = ObjectAnimator.ofFloat<View>(nextView, View.TRANSLATION_X, 0f)
        nextMover.setInterpolator(sAccelerator)
        nextMover.setDuration(ToonGameActivity.MEDIUM_DURATION)
        val nextSkewer = ObjectAnimator.ofFloat(nextView, "skewX", 0f)
        nextSkewer.setInterpolator(sOvershooter)

        val moverSet = AnimatorSet();
        moverSet.playTogether(currentMover, nextMover);
        val fullSet =  AnimatorSet();
        fullSet.playSequentially(currentSkewer, moverSet, nextSkewer);
        fullSet.addListener(object  :AnimatorListenerAdapter() {

            override fun onAnimationEnd(anim:Animator) {
                currentView.mSkewX = 0f
                currentView.setVisibility(View.GONE);
                currentView.setTranslationX(0f);
                if (endAction != null) {
                    endAction.run();
                }
            }
        })
        fullSet.start()
    }

    companion object {
        private val sAccelerator = AccelerateInterpolator()
        private val sLinearInterpolator = LinearInterpolator()
        private val NAME_STATE = 0
        private val DIFFICULTY_STATE = 1
        private val CREDIT_STATE = 2
        private val sOvershooter = OvershootInterpolator()
        private val sDecelerator = DecelerateInterpolator()

    }
}
