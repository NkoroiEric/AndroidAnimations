package com.androidanimations.curvedMotion

/**
 * Created by scaleup on 07/11/17.
 */

/**
 * A class that holds information about a location and how the path should get to that
 * location from the previous path location (if any). Any PathPoint holds the information for
 * its location as well as the instructions on how to traverse the preceding interval from the
 * previous location.
 */
class PathPoint {

    //locus of this pathpoint
    var mX = -1f
    var mY = -1f

    //first control point if any for a PathPoint of type CURVE
    var mControl0X = -1f
    var mControl0Y =-1f

    //second control point if any for a PathPoint of type CURVE
    var mControl1X = -1f
    var mControl1Y = -1f

    //the motion described by the path to get from the previous {@code PathPoint}
    //in an AnimatorPath to the location of this PathPoint. This can be one of
    //MOVE, LINE OR CURVE
    var mOperation = 0

    /**
     * Line/Move constructor
     */
    private constructor(operation: Int, x: Float, y: Float){
        this.mOperation = operation
        this.mX = x
        this.mY = y
    }

    //curve constructor
    private constructor(c0X: Float, c0Y : Float, c1X : Float, c1Y : Float, x : Float, y : Float){
        this.mControl0X = c0X
        this.mControl0Y = c0Y
        this.mControl1X = c1X
        this.mControl1Y = c1Y
        this.mX = x
        this.mY = y
        this.mOperation = CURVE
    }




    companion object {
        /**
         * The possible path operations that describe how to move from a preceding PathPoint to the
         * location described by this PathPoint.
         */
        val MOVE = 0
        val LINE = 1
        val CURVE = 2

        /**
         * Constructs and returns a PathPoint object that describes a line to the given xy location.
         */
        fun lineTo(x : Float, y : Float): PathPoint{
            return PathPoint(LINE, x,y)
        }

        /**
         * Constructs and returns a PathPoint object that describes a curve to the given xy location
         * with the control points at c0 and c1.
         */
        fun curveTo(c0X: Float, c0Y: Float, c1X: Float, c1Y: Float, x: Float, y: Float):PathPoint{
            return PathPoint(c0X, c0Y,c1X,c1Y,x,y)
        }

        /**
         * Constructs and returns a PathPoint object that describes a discontinuous move to the given
         * xy location.
         */
        fun moveTo(x: Float, y: Float):PathPoint{
            return PathPoint(MOVE, x,y)
        }
    }
}
