package com.androidanimations.activityAnimations

import android.content.res.Resources
import com.androidanimations.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory




/**
 * Created by scaleup on 07/11/17.
 */
class BitmapUtils {
    var mPhotos = intArrayOf(R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4)

    var mDescriptions = arrayOf("This picture was taken while sunbathing in a natural hot spring, which was " +
            "unfortunately filled with acid, which is a lasting memory from that trip, whenever I " +
            "I look at my own skin.", "I took this shot with a pinhole camera mounted on a tripod constructed out of " +
            "soda straws. I felt that that combination best captured the beauty of the landscape " +
            "in juxtaposition with the detritus of mankind.", "I don't remember where or when I took this picture. All I know is that I was really " + "drunk at the time, and I woke up without my left sock.", "Right before I took this picture, there was a busload of school children right " +
            "in my way. I knew the perfect shot was coming, so I quickly yelled 'Free candy!!!' " +
            "and they scattered.")

    /**
     * Load pictures and descriptions. A real app wouldn't do it this way, but that's
     * not the point of this animation demo. Loading asynchronously is a better way to go
     * for what can be time-consuming operations.
     */
    fun loadPhotos(resources: Resources): ArrayList<PictureData> {
        val pictures = ArrayList<PictureData>()
        for (i in 0..29) {
            val resourceId = mPhotos[(Math.random() * mPhotos.size) as Int]
            val bitmap = getBitmap(resources, resourceId)
            val thumbnail = getThumbnail(bitmap, 200)
            val description = mDescriptions[(Math.random() * mDescriptions.size) as Int]
            pictures.add(PictureData(resourceId, description, thumbnail))
        }
        return pictures
    }

    /**
     * Create and return a thumbnail image given the original source bitmap and a max
     * dimension (width or height).
     */
    private fun getThumbnail(original: Bitmap, maxDimension: Int): Bitmap {
        val width = original.width
        val height = original.height
        val scaledWidth: Int
        val scaledHeight: Int
        if (width >= height) {
            val scaleFactor = maxDimension.toFloat() / width
            scaledWidth = 200
            scaledHeight = (scaleFactor * height).toInt()
        } else {
            val scaleFactor = maxDimension.toFloat() / height
            scaledWidth = (scaleFactor * width).toInt()
            scaledHeight = 200
        }

        return Bitmap.createScaledBitmap(original, scaledWidth, scaledHeight, true)
    }



    companion object {
        var sBitmapResourceMap = HashMap<Int, Bitmap>()

        /**
         * Utility method to get bitmap from cache or, if not there, load it
         * from its resource.
         */
        fun getBitmap(resources: Resources, resourceId: Int): Bitmap {
            var bitmap = sBitmapResourceMap[resourceId]
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(resources, resourceId)
                sBitmapResourceMap.put(resourceId, bitmap)
            }
            return bitmap!!
        }
    }
}