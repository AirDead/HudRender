@file:Suppress("NOTHING_TO_INLINE")

package ru.airdead.hudrender.utility

import kotlin.math.sqrt

/**
 * Represents a 2-dimensional vector.
 *
 * @property x The x-coordinate of the vector.
 * @property y The y-coordinate of the vector.
 */
data class V2(
    var x: Double = 0.0,
    var y: Double = 0.0
) {
    /**
     * Interpolates between this vector and the given vector by the specified progress.
     *
     * @param other The vector to interpolate towards.
     * @param progress The interpolation progress, where 0.0 represents this vector and 1.0 represents the other vector.
     * @return A new vector that is the result of the interpolation.
     */
    fun interpolate(other: V2, progress: Double) = V2(
        x + (other.x - x) * progress,
        y + (other.y - y) * progress
    )
}

/**
 * Represents a 3-dimensional vector.
 *
 * @property x The x-coordinate of the vector.
 * @property y The y-coordinate of the vector.
 * @property z The z-coordinate of the vector.
 */
data class V3(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0
) {
    /**
     * Creates a copy of this vector.
     *
     * @return A new vector that is a copy of this vector.
     */
    fun copy() = V3(x, y, z)

    /**
     * Interpolates between this vector and the given vector by the specified progress.
     *
     * @param other The vector to interpolate towards.
     * @param progress The interpolation progress, where 0.0 represents this vector and 1.0 represents the other vector.
     * @return A new vector that is the result of the interpolation.
     */
    fun interpolate(other: V3, progress: Double) = V3(
        x + (other.x - x) * progress,
        y + (other.y - y) * progress,
        z + (other.z - z) * progress
    )

    /**
     * Adds the given vector to this vector.
     *
     * @param other The vector to add.
     * @return A new vector that is the sum of this vector and the given vector.
     */
    fun add(other: V3) = V3(x + other.x, y + other.y, z + other.z)

    /**
     * Subtracts the given vector from this vector.
     *
     * @param other The vector to subtract.
     * @return A new vector that is the difference between this vector and the given vector.
     */
    fun subtract(other: V3) = V3(x - other.x, y - other.y, z - other.z)

    /**
     * Adds the given vector to this vector using the + operator.
     *
     * @param other The vector to add.
     * @return A new vector that is the sum of this vector and the given vector.
     */
    operator fun plus(other: V3) = V3(x + other.x, y + other.y, z + other.z)

    /**
     * Subtracts the given vector from this vector using the - operator.
     *
     * @param other The vector to subtract.
     * @return A new vector that is the difference between this vector and the given vector.
     */
    operator fun minus(other: V3) = V3(x - other.x, y - other.y, z - other.z)

    /**
     * Multiplies this vector by the given scalar.
     *
     * @param scalar The scalar to multiply by.
     * @return A new vector that is the product of this vector and the scalar.
     */
    fun multiply(scalar: Double) = V3(x * scalar, y * scalar, z * scalar)

    /**
     * Multiplies this vector by the given vector component-wise.
     *
     * @param other The vector to multiply by.
     * @return A new vector that is the product of this vector and the given vector.
     */
    fun multiply(other: V3) = V3(x * other.x, y * other.y, z * other.z)

    /**
     * Calculates the length of this vector.
     *
     * @return The length of this vector.
     */
    fun length() = sqrt(x * x + y * y + z * z)
}

/**
 * Represents a rotation in degrees.
 *
 * @property degrees The rotation in degrees.
 */
open class Rotation(
    var degrees: Float
) {
    /**
     * Creates a copy of this rotation.
     *
     * @return A new rotation that is a copy of this rotation.
     */
    fun copy() = Rotation(degrees)

    /**
     * Interpolates between this rotation and the given rotation by the specified progress.
     *
     * @param other The rotation to interpolate towards.
     * @param progress The interpolation progress, where 0.0 represents this rotation and 1.0 represents the other rotation.
     * @return A new rotation that is the result of the interpolation.
     */
    fun interpolate(other: Rotation, progress: Double) = Rotation(
        degrees + (other.degrees - degrees) * progress.toFloat()
    )
}

/**
 * Creates a 3-dimensional vector from two numbers, with the z-coordinate set to 0.0.
 *
 * @receiver The x-coordinate of the vector.
 * @param other The y-coordinate of the vector.
 * @return A new 3-dimensional vector.
 */
inline infix fun Number.x(other: Number): V3 = V3(this.toDouble(), other.toDouble(), 0.0)