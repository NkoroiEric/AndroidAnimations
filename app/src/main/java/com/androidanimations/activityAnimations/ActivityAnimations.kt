package com.androidanimations.activityAnimations

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import com.androidanimations.R
import kotlinx.android.synthetic.main.activity_animations.*


/**
 * This example shows how to create a custom activity animation when you want something more
 * than window animations can provide. The idea is to disable window animations for the
 * activities and to instead launch or return from the sub-activity immediately, but use
 * property animations inside the activities to customize the transition.
 *
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on the DevBytes playlist in the androiddevelopers channel on YouTube at
 * https://www.youtube.com/playlist?list=PLWz5rJ2EKKc_XOgcRukSoKKjewFJZrKV0.
 */
class ActivityAnimations : AppCompatActivity() {

    lateinit var mGridLayout: GridLayout
    var mPicturesData = HashMap<ImageView, PictureData>()
    var mBitmapUtils = BitmapUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animations)

        //GrayScale filter used on all thumbnails
        val grayMatrix = ColorMatrix()
        grayMatrix.setSaturation(0f)
        val grayScaleFilter = ColorMatrixColorFilter(grayMatrix)

        mGridLayout = gridLayout
        mGridLayout.columnCount = 3
        mGridLayout.useDefaultMargins = true

        //add all photo thumbnails to layout
        val resources = getResources()
        val pictures = mBitmapUtils.loadPhotos(resources)
        for (i in 0 until pictures.size){
            val pictureData = pictures.get(i)
            val thumbnailDrawable = BitmapDrawable(resources, pictureData.thumbnail)
            thumbnailDrawable.colorFilter = grayScaleFilter
            val imageView = ImageView(this)
            imageView.setOnClickListener(thumbnailClickListener)
            imageView.setImageDrawable(thumbnailDrawable)
            mPicturesData.put(imageView, pictureData)
            mGridLayout.addView(imageView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_better_window_animations, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.menu_slow){
            sAnimatorScale = if (item.isChecked) 1f else 5f
            item.isChecked = !item.isChecked
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * When the user clicks a thumbnail, bundle up information about it and launch the
     * details activity.
     */
    private val  thumbnailClickListener : View.OnClickListener = View.OnClickListener { v ->
        // Interesting data to pass across are the thumbnail size/location, the
        // resourceId of the source bitmap, the picture description, and the
        // orientation (to avoid returning back to an obsolete configuration if
        // the device rotates again in the meantime)
        val screenLocation = IntArray(2)
        v.getLocationInWindow(screenLocation)
        val info = mPicturesData[v]
        val subActivity = Intent(this, PictureDetailsActivity::class.java)
        val orientation = resources.configuration.orientation
        subActivity
                .putExtra(PACKAGE + ".orientation", orientation)
                .putExtra(PACKAGE + ".resourceId", info!!.resourceId)
                .putExtra(PACKAGE + ".left", screenLocation[0])
                .putExtra(PACKAGE + ".top", screenLocation[1])
                .putExtra(PACKAGE + ".width", v.width)
                .putExtra(PACKAGE + ".height",v.height)
                .putExtra(PACKAGE + ".description", info.description)
        startActivity(subActivity)
        // Override transitions: we don't want the normal window animation in addition
        // to our custom one
        overridePendingTransition(0, 0);
    }
    companion object {
        private val PACKAGE = "com.example.android.activityanim"
        var sAnimatorScale = 1f

    }
}
