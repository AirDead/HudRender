@file:Suppress("UNCHECKED_CAST")

package ru.airdead.hudrenderer.animation

import kotlin.math.min

/**
 * A class representing an animation that interpolates between two states over a specified duration.
 *
 * @param T The type of the states being interpolated.
 * @property startState The initial state of the animation.
 * @property endState The final state of the animation.
 * @property duration The duration of the animation in seconds.
 * @property easing A function that defines the easing curve of the animation.
 * @property applyProperties A function that applies the interpolated properties to the target.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Animation<T>(
    val startState: T,
    val endState: T,
    val duration: Double,
    val easing: (Double) -> Double,
    val applyProperties: (T) -> Unit
) {
    /**
     * The elapsed time since the start of the animation.
     */
    var elapsedTime = 0.0
        private set

    /**
     * Updates the animation state based on the time delta since the last update.
     *
     * @param tickDelta The time delta since the last update in seconds.
     * @return `true` if the animation has completed, `false` otherwise.
     */
    fun update(tickDelta: Float): Boolean {
        elapsedTime += tickDelta.toDouble()
        val progress = min(elapsedTime / duration, 1.0)
        applyProperties(interpolateProperties(startState, endState, easing(progress)))
        return progress >= 1.0
    }

    /**
     * Resets the animation to its initial state.
     *
     * @return The current instance of the `Animation` class.
     */
    fun reset(): Animation<T> {
        elapsedTime = 0.0
        return this
    }

    /**
     * Interpolates between the start and end states based on the given progress.
     *
     * @param startState The initial state.
     * @param endState The final state.
     * @param progress The progress of the animation, ranging from 0.0 to 1.0.
     * @return The interpolated state.
     * @throws IllegalArgumentException if the property type is unknown.
     */
    private fun interpolateProperties(startState: T, endState: T, progress: Double): T {
        return when (startState) {
            is ElementProperties -> startState.interpolate(endState as ElementProperties, progress) as T
            else -> throw IllegalArgumentException("Unknown property type")
        }
    }
}