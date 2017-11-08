package com.androidanimations.activityAnimations

import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.animation.TimeInterpolator
import android.graphics.Color
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import android.graphics.drawable.ColorDrawable
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import android.view.ViewTreeObserver
import android.widget.ImageView
import kotlinx.android.synthetic.main.picture_info.*


/**
 * This sub-activity shows a zoomed-in view of a specific photo, along with the
 * picture's text description. Most of the logic is for the animations that will
 * be run when the activity is being launched and exited. When launching,
 * the large version of the picture will resize from the thumbnail version in the
 * main activity, colorizing it from the thumbnail's grayscale version at the
 * same time. Meanwhile, the black background of the activity will fade in and
 * the description will eventually slide into place. The exit animation runs all
 * of this in reverse.
 *
 */
class PictureDetailsActivity : AppCompatActivity() {

    private lateinit var mBitmapDrawable: BitmapDrawable
    private val colorizerMatrix = ColorMatrix()
    lateinit var mBackground: ColorDrawable
    var mLeftDelta: Int = 0
    var mTopDelta: Int = 0
    var mWidthScale: Float = 0.toFloat()
    var mHeightScale: Float = 0.toFloat()
    private lateinit var mImageView: ImageView
    private lateinit var mTextView: TextView
    private lateinit var mTopLevelLayout: FrameLayout
    private lateinit var mShadowLayout: ShadowLayout
    private var mOriginalOrientation: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImageView = imageView
        mTopLevelLayout = topLevelLayout
        mShadowLayout = shadowLayout
        mTextView = description

        //Retrieve the data we need for picture/ description to display
        //and the thumbnail to animate it from
        val bundle = intent.extras
        val bitmap = BitmapUtils.getBitmap(resources, bundle.getInt(PACKAGE_NAME + ".resourceId"))
        val description = bundle.getString(PACKAGE_NAME + ".description")
        val thumbnailTop = bundle.getInt(PACKAGE_NAME + ".top");
        val  thumbnailLeft = bundle.getInt(PACKAGE_NAME + ".left");
        val thumbnailWidth = bundle.getInt(PACKAGE_NAME + ".width");
        val thumbnailHeight = bundle.getInt(PACKAGE_NAME + ".height");
        mOriginalOrientation = bundle.getInt(PACKAGE_NAME + ".orientation");

        mBitmapDrawable = BitmapDrawable(resources, bitmap)
        mImageView.setImageDrawable(mBitmapDrawable)
        mTextView.text = description

        mBackground = ColorDrawable(Color.BLACK)
        mTopLevelLayout.background = mBackground


        // Only run the animation if we're coming from the parent activity, not if
        // we're recreated automatically by the window manager (e.g., device rotation)
        if (savedInstanceState == null){
            val observer = mImageView.viewTreeObserver
            observer.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
                override fun onPreDraw(): Boolean {
                    mImageView.viewTreeObserver.removeOnPreDrawListener(this)

                    //Figure out where the thumbnail and full size version are , relative
                    //to the screen and each other
                    val screenLocation = IntArray(2)
                    mImageView.getLocationInWindow(screenLocation)
                    mLeftDelta = thumbnailLeft - screenLocation[0]
                    mTopDelta = thumbnailTop - screenLocation[1]

                    //scale factors to make the large version the same as the thumbnail
                    mWidthScale = (thumbnailWidth / mImageView.width).toFloat()
                    mHeightScale = (thumbnailHeight /mImageView.height).toFloat()

                    runEnterAnimation()
                    return true
                }

            })
        }

    }

    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location, colorizing it in parallel. In parallel, the background of the
     * activity is fading in. When the pictue is in place, the text description
     * drops down.
     */
    private fun runEnterAnimation() {
        val duration = ANIM_DURATION * ActivityAnimations.sAnimatorScale
        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        mImageView.setPivotX(0f);
        mImageView.setPivotY(0f);
        mImageView.setScaleX(mWidthScale);
        mImageView.setScaleY(mHeightScale);
        mImageView.setTranslationX(mLeftDelta.toFloat());
        mImageView.setTranslationY(mTopDelta.toFloat());

        //we'll fade the text in later
        mTextView.alpha = 0f

        //Animate scale and translation to go from thumbnail to full size
        mImageView.animate().setDuration(duration.toLong())
                .scaleX(1f).scaleY(1f)
                .translationX(0f).translationY(0f)
                .setInterpolator(sDecelerator)
                .withEndAction {
                    // Animate the description in after the image animation
                    // is done. Slide and fade the text in from underneath
                    // the picture.
                    mTextView.translationY = (-mTextView.height).toFloat()
                    mTextView.animate().setDuration((duration/2).toLong())
                            .translationY(0f).alpha(1f)
                            .interpolator = sDecelerator
                }
        //Fade in the black background
        val bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0,255)
        bgAnim.setDuration(duration.toLong())
        bgAnim.start()

        // Animate a color filter to take the image from grayscale to full color.
        // This happens in parallel with the image scaling and moving into place.
        val colorizer = ObjectAnimator.ofFloat(this, "saturation", 0f,1f)
        colorizer.setDuration(duration.toLong())
        colorizer.start()
    }

    /**
     * The exit animation is basically a reverse of the enter animation, except that if
     * the orientation has changed we simply scale the picture back into the center of
     * the screen.
     *
     * @param endAction This action gets run after the animation completes (this is
     * when we actually switch activities)
     */
    fun runExitAnimation(endAction : Runnable){
        val duration = (ANIM_DURATION * ActivityAnimations.sAnimatorScale).toLong();

        // No need to set initial values for the reverse animation; the image is at the
        // starting size/location that we want to start from. Just animate to the
        // thumbnail size/location that we retrieved earlier

        // Caveat: configuration change invalidates thumbnail positions; just animate
        // the scale around the center. Also, fade it out since it won't match up with
        // whatever's actually in the center
        val fadeOut:Boolean
        if (getResources().getConfiguration().orientation != mOriginalOrientation) {
            mImageView.setPivotX((mImageView.getWidth() / 2).toFloat());
            mImageView.setPivotY((mImageView.getHeight() / 2).toFloat());
            mLeftDelta = 0;
            mTopDelta = 0;
            fadeOut = true;
        } else {
            fadeOut = false;
        }

        //First, slide/fade text out of the way
        mTextView.animate().translationY((-mTextView.height).toFloat()).alpha(0f)
                .setDuration(duration)
                .setInterpolator(sAccelerator)
                .withEndAction {
                    //Animate image back to thumbnail size/location
                    mImageView.animate().setDuration(duration)
                            .scaleX(mWidthScale).scaleY(mHeightScale)
                            .translationX(mLeftDelta.toFloat()).translationY(mTopDelta.toFloat())
                            .withEndAction(endAction)
                    if (fadeOut){
                        mImageView.animate().alpha(0f)
                    }
                    //Fade out background
                    val bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0)
                    bgAnim.setDuration(duration)
                    bgAnim.start()

                    //Animate the shadow of the image
                    val shadowAnim = ObjectAnimator.ofFloat(mShadowLayout, "shadowDepth",1f,0f)
                    shadowAnim.setDuration(duration)
                    shadowAnim.start()

                    //Animate a color filter to take the image back to grayScale
                    //in parallel with the image scaling and moving into place
                    val colorizer = ObjectAnimator.ofFloat(this, "saturation", 1f,0f)
                    colorizer.setDuration(duration)
                    colorizer.start()
                }
    }


    /**
     * Overriding this method allows us to run our exit animation first, then exiting
     * the activity when it is complete.
     */
    override fun onBackPressed() {
        runExitAnimation(Runnable {
            // *Now* go ahead and exit the activity
            finish()
        })
    }

    /**
     * This is called by the colorizing animator. It sets a saturation factor that is then
     * passed onto a filter on the picture's drawable.
     * @param value
     */
    fun setSaturation(value : Float){
        colorizerMatrix.setSaturation(value)
        val colorizerFilter = ColorMatrixColorFilter(colorizerMatrix)
        mBitmapDrawable.colorFilter = colorizerFilter
    }

    override fun finish() {
        super.finish()

        // override transitions to skip the standard window animations
        overridePendingTransition(0, 0)
    }

    companion object {
        private val sDecelerator = DecelerateInterpolator()
        private val sAccelerator = AccelerateInterpolator()
        private val PACKAGE_NAME = "com.androidanimations"
        private val ANIM_DURATION = 500
    }
}
