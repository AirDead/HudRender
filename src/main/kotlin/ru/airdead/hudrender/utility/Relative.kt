package ru.airdead.hudrender.utility

/**
 * Predefined positions for alignment and anchoring.
 */
@JvmField val TOP_LEFT: V3 = V3(0.0, 0.0)
@JvmField val TOP: V3 = V3(0.5, 0.0)
@JvmField val TOP_RIGHT: V3 = V3(1.0, 0.0)
@JvmField val LEFT: V3 = V3(0.0, 0.5)
@JvmField val CENTER: V3 = V3(0.5, 0.5)
@JvmField val RIGHT: V3 = V3(1.0, 0.5)
@JvmField val BOTTOM_LEFT: V3 = V3(0.0, 1.0)
@JvmField val BOTTOM: V3 = V3(0.5, 1.0)
@JvmField val BOTTOM_RIGHT: V3 = V3(1.0, 1.0)

/**
 * Object containing predefined relative positions for alignment and anchoring.
 */
object Relative {
    @JvmField val TOP_LEFT: V3 = V3(0.0, 0.0)
    @JvmField val TOP: V3 = V3(0.5, 0.0)
    @JvmField val TOP_RIGHT: V3 = V3(1.0, 0.0)
    @JvmField val LEFT: V3 = V3(0.0, 0.5)
    @JvmField val CENTER: V3 = V3(0.5, 0.5)
    @JvmField val RIGHT: V3 = V3(1.0, 0.5)
    @JvmField val BOTTOM_LEFT: V3 = V3(0.0, 1.0)
    @JvmField val BOTTOM: V3 = V3(0.5, 1.0)
    @JvmField val BOTTOM_RIGHT: V3 = V3(1.0, 1.0)
}
