package com.androidanimations.curvedMotion

import android.animation.TypeEvaluator

/**
 * Created by scaleup on 07/11/17.
 */

/**
 * This evaluator interpolates between two PathPoint values given the value t, the
 * proportion traveled between those points. The value of the interpolation depends
 * on the operation specified by the endValue (the operation for the interval between
 * PathPoints is always specified by the end point of that interval).
 */
class PathEvaluator : TypeEvaluator<PathPoint> {
    override fun evaluate(t: Float, startValue: PathPoint, endValue: PathPoint): PathPoint {
        val x: Float
        val y: Float
        if (endValue.mOperation == PathPoint.CURVE) {
            val oneMinusT = 1 - t
            x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                    3f * oneMinusT * oneMinusT * t * endValue.mControl0X +
                    3f * oneMinusT * t * t * endValue.mControl1X +
                    t * t * t * endValue.mX
            y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                    3f * oneMinusT * oneMinusT * t * endValue.mControl0Y +
                    3f * oneMinusT * t * t * endValue.mControl1Y +
                    t * t * t * endValue.mY
        } else if (endValue.mOperation == PathPoint.LINE) {
            x = startValue.mX + t * (endValue.mX - startValue.mX)
            y = startValue.mY + t * (endValue.mY - startValue.mY)
        } else {
            x = endValue.mX
            y = endValue.mY
        }
        return PathPoint.moveTo(x, y)
    }
}