package ru.airdead.hudrenderer.element

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import ru.airdead.hudrenderer.UIEngine
import ru.airdead.hudrenderer.utility.V3

/**
 * Class representing a text element.
 * Inherits from [AbstractElement].
 */
class TextElement : AbstractElement() {

    /**
     * The content of the text element.
     * When the content is updated, the size is also updated.
     */
    var content: String = ""
        set(value) {
            field = value
            updateSize()
        }

    /**
     * Indicates whether the text should be drawn with a shadow.
     */
    var shadow = false

    /**
     * Indicates whether the text element should automatically fit its content.
     * When enabled, the size is updated based on the content.
     */
    var autoFit: Boolean = false
        set(value) {
            field = value
            if (value) updateSize()
        }

    /**
     * Updates the size of the text element based on its content and settings.
     */
    private fun updateSize() {
        size = if (autoFit) {
            val parentWidth = lastParent?.size?.x ?: size.x
            V3(parentWidth, size.y, size.z)
        } else {
            val textWidth = calculateTextWidth(content)
            V3(textWidth, size.y, size.z)
        }
    }

    /**
     * Calculates the width of the text.
     *
     * @param text The text whose width is to be calculated.
     * @return The width of the text.
     */
    private fun calculateTextWidth(text: String): Double {
        val textRenderer: TextRenderer = UIEngine.clientApi.minecraft().textRenderer
        return textRenderer.getWidth(Text.of(text)).toDouble()
    }

    /**
     * Renders the text element.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        if (size.x <= 0 || size.y <= 0) return

        val textRenderer: TextRenderer = UIEngine.clientApi.minecraft().textRenderer
        val textColor = color
        drawContext.drawText(textRenderer, content, renderLocation.x.toInt(), renderLocation.y.toInt(), textColor.toInt(), shadow)
    }
}
