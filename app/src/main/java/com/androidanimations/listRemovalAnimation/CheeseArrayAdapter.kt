package com.androidanimations.listRemovalAnimation

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter


/**
 * Created by scaleup on 05/11/17.
 */
class CheeseArrayAdapter(context: Context, textViewResourceId: Int,
                         objects: List<String>, internal var mTouchListener: View.OnTouchListener) : ArrayAdapter<String>(context, textViewResourceId, objects) {

    internal var mIdMap = HashMap<String, Int>()

    init {
        for (i in objects.indices) {
            mIdMap.put(objects[i], i)
        }
    }

    override fun getItemId(position: Int): Long {
        val item = getItem(position)
        return mIdMap[item]!!.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if (view !== convertView) {
            // Add touch listener to every new view to track swipe motion
            view.setOnTouchListener(mTouchListener)
        }
        return view
    }

}
