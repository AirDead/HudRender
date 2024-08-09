package ru.airdead.hudrenderer.element.line

import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.utility.V3
import kotlin.math.roundToInt

class DashedLineElement : AbstractElement() {
    var speed = 0.01

    var startPoint = V3(0.0, 0.0, 0.0)
    var endPoint = V3(100.0, 0.0, 0.0)

    val pointCount = 10

    var progress = 0.0
        private set

    private var lastTickProgress = 0.0

    var startElement: AbstractElement? = null
    var endElement: AbstractElement? = null

    override fun render(drawContext: DrawContext, tickDelta: Float) {
        updateLinePoints()

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

    private fun interpolateProgress(tickDelta: Float): Double {
        return if (progress < lastTickProgress) {
            progress
        } else {
            lastTickProgress + (progress - lastTickProgress) * tickDelta
        }
    }

    private fun updateProgress() {
        lastTickProgress = progress
        progress += speed
        if (progress >= 1.0) {
            progress = 0.0
            lastTickProgress = 0.0
        }
    }

    private fun updateLinePoints() {
        startElement?.let {
            startPoint = it.renderLocation.add(it.size.multiply(it.origin))
        }

        endElement?.let {
            endPoint = it.renderLocation.add(it.size.multiply(it.origin))
        }
    }
}