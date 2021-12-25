package io.github.muqhc.regionmaker.util

open class Polygon {
    val points: MutableList<Vector2D> = mutableListOf()

    fun addPoint(point: Vector2D) = points.add(point)
    fun addPoint(x: Double,y: Double) = addPoint(Vector2D(x,y))
    fun addPoint(xy: Pair<Double,Double>) = addPoint(Vector2D(xy))

    fun insertPoint(index: Int, point: Vector2D) = points.add(index,point)
    fun insertPoint(index: Int, x: Double,y: Double) = insertPoint(index,Vector2D(x,y))
    fun insertPoint(index: Int, xy: Pair<Double,Double>) = insertPoint(index,Vector2D(xy))
}