@file:Suppress("NOTHING_TO_INLINE")

package ru.airdead.hudrenderer.utility

data class V2(
    var x: Double = 0.0,
    var y: Double = 0.0
) {
    fun interpolate(other: V2, progress: Double) = V2(
        x + (other.x - x) * progress,
        y + (other.y - y) * progress
    )
}

data class V3(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0
) {
    fun copy() = V3(x, y, z)

    fun interpolate(other: V3, progress: Double) = V3(
        x + (other.x - x) * progress,
        y + (other.y - y) * progress,
        z + (other.z - z) * progress
    )
}

open class Rotation(
    var degrees: Float
) {
    fun copy() = Rotation(degrees)

    fun interpolate(other: Rotation, progress: Double) = Rotation(
        degrees + (other.degrees - degrees) * progress.toFloat()
    )
}

inline infix fun Number.x(other: Number): V3 = V3(this.toDouble(), other.toDouble(), 0.0)