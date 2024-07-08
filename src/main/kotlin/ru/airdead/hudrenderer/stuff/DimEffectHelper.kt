package ru.airdead.hudrenderer.stuff

import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.utility.Color

object DimEffectHelper {
    fun applyDimEffect(drawContext: DrawContext, x1: Int, y1: Int, x2: Int, y2: Int, dimStrength: Float, color: Color) {
        val startColor = getBackgroundColor(color, dimStrength)
        val endColor = getBackgroundColor(color, dimStrength)

        drawContext.fillGradient(x1, y1, x2, y2, startColor, endColor)
    }

    private fun getBackgroundColor(color: Color, dimStrength: Float): Int {
        val adjustedColor = color.copy().apply {
            alpha *= dimStrength
        }
        return adjustedColor.toInt()
    }
}
