package com.androidanimations.curvedMotion

/**
 * Created by scaleup on 07/11/17.
 */

/**
 * A simple Path object that holds information about the points along
 * a path. The API allows you to specify a move location (which essentially
 * jumps from the previous point in the path to the new one), a line location
 * (which creates a line segment from the previous location) and a curve
 * location (which creates a B�zier curve from the previous location).
 */
class AnimatorPath {

    //the points in the path
    val mPoints = ArrayList<PathPoint>()


    /**
     * Move from the current path point to the new one
     * specified by x and y. This will create a discontinuity if this point is
     * neither the first point in the path nor the same as the previous point
     * in the path.
     */
    fun moveTo(x : Float,y : Float){
        mPoints.add(PathPoint.moveTo(x,y))
    }

    /**
     * Create a straight line from the current path point to the new one
     * specified by x and y.
     */
    fun lineTo(x : Float, y : Float){
        mPoints.add(PathPoint.lineTo(x,y))
    }

    /**
     * Create a quadratic B�zier curve from the current path point to the new one
     * specified by x and y. The curve uses the current path location as the first anchor
     * point, the control points (c0X, c0Y) and (c1X, c1Y), and (x, y) as the end anchor.
     */
    fun curveTo(c0X : Float, c0Y : Float, c1X : Float, c1Y : Float, x : Float, y : Float){
        mPoints.add(PathPoint.curveTo(c0X, c0Y, c1X, c1Y, x,y))
    }


    /**
     * Returns a Collection of PathPoint objects that describe all points in the path.
     */
    fun getPoints() : ArrayList<PathPoint>{
        return mPoints
    }
}