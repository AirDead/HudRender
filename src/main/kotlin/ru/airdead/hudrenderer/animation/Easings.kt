@file:Suppress("SpellCheckingInspection")

package ru.airdead.hudrenderer.animation

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Functional interface representing an easing function.
 */
fun interface Easing {
    /**
     * Applies the easing function to the given value.
     *
     * @param value The input value to ease.
     * @return The eased value.
     */
    fun ease(value: Double): Double
}

/**
 * Object containing various easing functions.
 */
object Easings {

    private const val c1 = 1.70158
    private const val c2 = c1 * 1.525
    private const val c3 = c1 + 1.0
    private const val c4 = 2.0 * Math.PI / 3.0
    private const val c5 = 2.0 * Math.PI / 4.5

    /**
     * No easing, no acceleration.
     */
    val NONE: Easing = Easing { it }

    /**
     * Quadratic easing in - accelerating from zero velocity.
     */
    val QUAD_IN: Easing = powIn(2.0)

    /**
     * Quadratic easing out - decelerating to zero velocity.
     */
    val QUAD_OUT: Easing = powOut(2.0)

    /**
     * Quadratic easing in and out - acceleration until halfway, then deceleration.
     */
    val QUAD_BOTH: Easing = powBoth(2.0)

    /**
     * Cubic easing in - accelerating from zero velocity.
     */
    val CUBIC_IN: Easing = powIn(3.0)

    /**
     * Cubic easing out - decelerating to zero velocity.
     */
    val CUBIC_OUT: Easing = powOut(3.0)

    /**
     * Cubic easing in and out - acceleration until halfway, then deceleration.
     */
    val CUBIC_BOTH: Easing = powBoth(3.0)

    /**
     * Quartic easing in - accelerating from zero velocity.
     */
    val QUART_IN: Easing = powIn(4.0)

    /**
     * Quartic easing out - decelerating to zero velocity.
     */
    val QUART_OUT: Easing = powOut(4.0)

    /**
     * Quartic easing in and out - acceleration until halfway, then deceleration.
     */
    val QUART_BOTH: Easing = powBoth(4.0)

    /**
     * Quintic easing in - accelerating from zero velocity.
     */
    val QUINT_IN: Easing = powIn(5.0)

    /**
     * Quintic easing out - decelerating to zero velocity.
     */
    val QUINT_OUT: Easing = powOut(5.0)

    /**
     * Quintic easing in and out - acceleration until halfway, then deceleration.
     */
    val QUINT_BOTH: Easing = powBoth(5.0)

    /**
     * Sine easing in - accelerating from zero velocity.
     */
    val SINE_IN: Easing = Easing { 1.0 - cos(it * Math.PI / 2.0) }

    /**
     * Sine easing out - decelerating to zero velocity.
     */
    val SINE_OUT: Easing = Easing { sin(it * Math.PI / 2.0) }

    /**
     * Sine easing in and out - acceleration until halfway, then deceleration.
     */
    val SINE_BOTH: Easing = Easing { -(cos(Math.PI * it) - 1.0) / 2.0 }

    /**
     * Circular easing in - accelerating from zero velocity.
     */
    val CIRC_IN: Easing = Easing { 1.0 - sqrt(1.0 - it * it) }

    /**
     * Circular easing out - decelerating to zero velocity.
     */
    val CIRC_OUT: Easing = Easing { sqrt(1.0 - (it - 1.0).pow(2.0)) }

    /**
     * Circular easing in and out - acceleration until halfway, then deceleration.
     */
    val CIRC_BOTH: Easing = Easing {
        if (it < 0.5) {
            (1.0 - sqrt(1.0 - (2.0 * it).pow(2.0))) / 2.0
        } else {
            (sqrt(1.0 - (-2.0 * it + 2.0).pow(2.0)) + 1.0) / 2.0
        }
    }

    /**
     * Elastic easing in - accelerating from zero velocity.
     */
    val ELASTIC_IN: Easing = Easing {
        if (it == 0.0 || it == 1.0) {
            it
        } else {
            (-2.0).pow(10.0 * it - 10.0) * sin((it * 10.0 - 10.75) * c4)
        }
    }

    /**
     * Elastic easing out - decelerating to zero velocity.
     */
    val ELASTIC_OUT: Easing = Easing {
        if (it == 0.0 || it == 1.0) {
            it
        } else {
            2.0.pow(-10.0 * it) * sin((it * 10.0 - 0.75) * c4) + 1.0
        }
    }

    /**
     * Elastic easing in and out - acceleration until halfway, then deceleration.
     */
    val ELASTIC_BOTH: Easing = Easing {
        when {
            it == 0.0 || it == 1.0 -> it
            it < 0.5 -> -(2.0.pow(20.0 * it - 10.0) * sin((20.0 * it - 11.125) * c5)) / 2.0
            else -> 2.0.pow(-20.0 * it + 10.0) * sin((20.0 * it - 11.125) * c5) / 2.0 + 1.0
        }
    }

    /**
     * Exponential easing in - accelerating from zero velocity.
     */
    val EXPO_IN: Easing = Easing {
        if (it != 0.0) {
            2.0.pow(10.0 * it - 10.0)
        } else {
            it
        }
    }

    /**
     * Exponential easing out - decelerating to zero velocity.
     */
    val EXPO_OUT: Easing = Easing {
        if (it != 1.0) {
            1.0 - 2.0.pow(-10.0 * it)
        } else {
            it
        }
    }

    /**
     * Exponential easing in and out - acceleration until halfway, then deceleration.
     */
    val EXPO_BOTH: Easing = Easing {
        when {
            it == 0.0 || it == 1.0 -> it
            it < 0.5 -> 2.0.pow(20.0 * it - 10.0) / 2.0
            else -> (2.0 - 2.0.pow(-20.0 * it + 10.0)) / 2.0
        }
    }

    /**
     * Back easing in - accelerating from zero velocity.
     */
    val BACK_IN: Easing = Easing { c3 * it.pow(3.0) - c1 * it.pow(2.0) }

    /**
     * Back easing out - decelerating to zero velocity.
     */
    val BACK_OUT: Easing = Easing { 1.0 + c3 * (it - 1.0).pow(3.0) + c1 * (it - 1.0).pow(2.0) }

    /**
     * Back easing in and out - acceleration until halfway, then deceleration.
     */
    val BACK_BOTH: Easing = Easing {
        if (it < 0.5) {
            (2.0 * it).pow(2.0) * ((c2 + 1.0) * 2.0 * it - c2) / 2.0
        } else {
            ((2.0 * it - 2.0).pow(2.0) * ((c2 + 1.0) * (it * 2.0 - 2.0) + c2) + 2.0) / 2.0
        }
    }

    /**
     * Bounce easing out - decelerating to zero velocity.
     */
    val BOUNCE_OUT: Easing = Easing { x ->
        val n1 = 7.5625
        val d1 = 2.75
        when {
            x < 1.0 / d1 -> n1 * x * x
            x < 2.0 / d1 -> n1 * (x - 1.5 / d1) * (x - 1.5 / d1) + 0.75
            x < 2.5 / d1 -> n1 * (x - 2.25 / d1) * (x - 2.25 / d1) + 0.9375
            else -> n1 * (x - 2.625 / d1) * (x - 2.625 / d1) + 0.984375
        }
    }

    /**
     * Bounce easing in - accelerating from zero velocity.
     */
    val BOUNCE_IN: Easing = Easing { 1.0 - BOUNCE_OUT.ease(1.0 - it) }

    /**
     * Bounce easing in and out - acceleration until halfway, then deceleration.
     */
    val BOUNCE_BOTH: Easing = Easing {
        if (it < 0.5) {
            (1 - BOUNCE_OUT.ease(1.0 - 2.0 * it)) / 2.0
        } else {
            (1 + BOUNCE_OUT.ease(2.0 * it - 1.0)) / 2.0
        }
    }

    /**
     * Creates an easing function for the given power.
     *
     * @param n The power to use for the easing function.
     * @return The easing function.
     */
    private fun powIn(n: Double): Easing = Easing { it.pow(n) }

    /**
     * Creates an easing function for the given power.
     *
     * @param n The power to use for the easing function.
     * @return The easing function.
     */
    private fun powOut(n: Double): Easing = Easing { 1.0 - (1.0 - it).pow(n) }

    /**
     * Creates an easing function for the given power.
     *
     * @param n The power to use for the easing function.
     * @return The easing function.
     */
    private fun powBoth(n: Double): Easing = Easing {
        if (it < 0.5) {
            (2.0.pow(n - 1) * it.pow(n))
        } else {
            1.0 - (-2.0 * it + 2.0).pow(n) / 2.0
        }
    }
}