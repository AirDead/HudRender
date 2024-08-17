package ru.airdead.hudrender.element.line

import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrender.element.AbstractElement
import ru.airdead.hudrender.utility.V3
import kotlin.math.roundToInt

/**
 * A class representing a dashed line element in the HUD.
 * This element renders a dashed line between two points.
 */
@Suppress("MemberVisibilityCanBePrivate")
class DashedLineElement : AbstractElement() {
    /**
     * The speed at which the dashes move.
     */
    var speed = 0.01

    /**
     * The starting point of the dashed line.
     */
    var startPoint = V3(0.0, 0.0, 0.0)

    /**
     * The ending point of the dashed line.
     */
    var endPoint = V3(100.0, 0.0, 0.0)

    /**
     * The number of points (dashes) in the line.
     */
    val pointCount = 10

    /**
     * The current progress of the animation.
     */
    var progress = 0.0
        private set

    private var lastTickProgress = 0.0

    /**
     * Renders the dashed line element.
     * @param drawContext The drawing context.
     * @param tickDelta The delta time since the last tick.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        val interpolatedProgress = interpolateProgress(tickDelta)
        val dir = endPoint.subtract(startPoint).multiply(1.0 / pointCount)

        for (i in 0 until pointCount) {
            val offset = i + interpolatedProgress
            val pointPos = startPoint.add(dir.multiply(offset))
            val x = pointPos.x.roundToInt()
            val y = pointPos.y.roundToInt()
            drawContext.fill(x, y, x + 2, y + 2, color.toInt())
        }

        updateProgress()
    }

    /**
     * Interpolates the progress of the animation based on the tick delta.
     * @param tickDelta The delta time since the last tick.
     * @return The interpolated progress.
     */
    private fun interpolateProgress(tickDelta: Float): Double {
        return if (progress < lastTickProgress) {
            progress
        } else {
            lastTickProgress + (progress - lastTickProgress) * tickDelta
        }
    }

    /**
     * Updates the progress of the animation.
     */
    private fun updateProgress() {
        lastTickProgress = progress
        progress += speed
        if (progress >= 1.0) {
            progress = 0.0
            lastTickProgress = 0.0
        }
    }
}