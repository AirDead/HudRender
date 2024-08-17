@file:Suppress("UNCHECKED_CAST")

package ru.airdead.hudrender.animation

import ru.airdead.hudrender.utility.Color
import ru.airdead.hudrender.utility.Rotation
import ru.airdead.hudrender.utility.V3
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
     * Updates the animation state based on the tick delta.
     *
     * @param tickDelta The time delta since the last update.
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
     * Interpolates the properties between the start and end states based on the progress.
     *
     * @param startState The initial state of the animation.
     * @param endState The final state of the animation.
     * @param progress The progress of the animation, ranging from 0.0 to 1.0.
     * @return The interpolated state.
     */
    private fun interpolateProperties(startState: T, endState: T, progress: Double): T {
        return startState.apply {
            endState!!::class.members.forEach { member ->
                if (member is kotlin.reflect.KMutableProperty<*>) {
                    val startValue = member.getter.call(startState)
                    val endValue = member.getter.call(endState)

                    val interpolatedValue = when (startValue) {
                        is Color -> startValue.interpolate(endValue as Color, progress)
                        is V3 -> startValue.interpolate(endValue as V3, progress)
                        is Rotation -> startValue.interpolate(endValue as Rotation, progress)
                        else -> endValue
                    }

                    member.setter.call(this, interpolatedValue)
                }
            }
        }
    }
}