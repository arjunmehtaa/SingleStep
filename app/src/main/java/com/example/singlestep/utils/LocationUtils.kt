package com.example.singlestep.utils

import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

fun getBoundingBox(centerPoint: Pair<Double, Double>, distance: Double): List<Double> {
    val minLatLimit: Double
    val maxLatLimit: Double
    val minLonLimit: Double
    val maxLonLimit: Double
    val radLat: Double
    val radLon: Double
    var minLat: Double
    var maxLat: Double
    var minLon: Double
    var maxLon: Double
    val deltaLon: Double

    if (distance < 0) {
        return listOf() // Returning an empty list as an indication of illegal arguments
    }

    // Helper functions (degrees to radians)
    fun Number.degToRad(): Double = this.toDouble() * (Math.PI / 180)
    fun Number.radToDeg(): Double = this.toDouble() * (180 / Math.PI)

    // Coordinate limits
    minLatLimit = (-90).degToRad()
    maxLatLimit = (90).degToRad()
    minLonLimit = (-180).degToRad()
    maxLonLimit = (180).degToRad()

    // Earth's radius (km)
    val r: Double = 6378.1

    // Angular distance in radians on a great circle
    val radDist: Double = distance / r

    // Center point coordinates (deg)
    val degLat: Double = centerPoint.first
    val degLon: Double = centerPoint.second

    // Center point coordinates (rad)
    radLat = degLat.degToRad()
    radLon = degLon.degToRad()

    // Minimum and maximum latitudes for given distance
    minLat = radLat - radDist
    maxLat = radLat + radDist

    // Define deltaLon to help determine min and max longitudes
    deltaLon = asin(sin(radDist) / cos(radLat))

    if (minLat > minLatLimit && maxLat < maxLatLimit) {
        minLon = radLon - deltaLon
        maxLon = radLon + deltaLon

        if (minLon < minLonLimit) {
            minLon += 2 * PI
        }
        if (maxLon > maxLonLimit) {
            maxLon -= 2 * PI
        }
    } else {
        // A pole is within the given distance
        minLat = max(minLat, minLatLimit)
        maxLat = min(maxLat, maxLatLimit)
        minLon = minLonLimit
        maxLon = maxLonLimit
    }

    return listOf(
        minLat.radToDeg(),
        minLon.radToDeg(),
        maxLat.radToDeg(),
        maxLon.radToDeg()
    )
}