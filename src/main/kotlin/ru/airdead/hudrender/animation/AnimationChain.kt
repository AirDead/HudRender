package ru.airdead.hudrender.animation

import ru.airdead.hudrender.element.AbstractElement

/**
 * A chain of animations to be applied sequentially to an element.
 *
 * @property element The element to which the animations are applied.
 * @property initialAnimation The initial animation to start the chain.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class AnimationChain(private val element: AbstractElement, private val initialAnimation: Animation<ElementProperties>) {
    /**
     * List of animations in the chain.
     */
    val animations = mutableListOf<Animation<ElementProperties>>().apply { add(initialAnimation) }

    /**
     * Indicates whether the animation chain should loop.
     */
    var loop = false
        private set

    /**
     * Number of times the animation chain should repeat.
     */
    var repeatCount = 0
        private set

    /**
     * Current repeat count of the animation chain.
     */
    var currentRepeat = 0
        private set

    /**
     * Adds a new animation to the chain with the specified duration and easing.
     *
     * @param duration The duration of the animation.
     * @param easing The easing function to be used for the animation.
     * @param updateProperties A lambda to update the properties of the element.
     * @return The updated AnimationChain.
     */
    fun then(duration: Double, easing: Easing = Easings.NONE, updateProperties: AbstractElement.() -> Unit): AnimationChain {
        val lastState = animations.last().endState
        updateProperties(element)
        val newEndState = element.copyProperties()
        val animation = Animation(lastState, newEndState, duration * 20, easing::ease, initialAnimation.applyProperties)
        animations.add(animation)
        return this
    }

    /**
     * Adds a new animation to the chain with the specified duration and no property updates.
     *
     * @param duration The duration of the animation.
     * @return The updated AnimationChain.
     */
    fun then(duration: Double): AnimationChain {
        return then(duration, Easings.NONE) { }
    }

    /**
     * Adds a new animation to the chain with zero duration and no property updates.
     *
     * @return The updated AnimationChain.  
     */
    fun then(): AnimationChain {
        return then(0.0, Easings.NONE) { }
    }

    /**
     * Sets the animation chain to loop.
     *
     * @return The updated AnimationChain.
     */
    fun loop(): AnimationChain {
        loop = true
        return this
    }

    /**
     * Sets the number of times the animation chain should repeat.
     *
     * @param count The number of times to repeat the animation chain.
     * @return The updated AnimationChain.
     */
    fun repeat(count: Int = 1): AnimationChain {
        repeatCount = count
        return this
    }

    /**
     * Updates the current animation in the chain based on the tick delta.
     *
     * @param tickDelta The time delta since the last update.
     * @return True if all animations are complete, false otherwise.
     */
    fun update(tickDelta: Float): Boolean {
        if (animations.isEmpty()) return true
        val currentAnimation = animations.first()
        if (currentAnimation.update(tickDelta)) {
            animations.removeAt(0)
            if (loop || (repeatCount > 0 && currentRepeat < repeatCount)) {
                animations.add(currentAnimation.reset())
                if (repeatCount > 0) currentRepeat++
            }
        }
        return animations.isEmpty()
    }
}