package ru.airdead.hudrenderer.element.text

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import ru.airdead.hudrenderer.HudEngine
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.utility.Color
import ru.airdead.hudrenderer.utility.V3

/**
 * Base class for text elements, handling common properties and rendering logic.
 */
abstract class BaseTextElement : AbstractElement() {

    /**
     * The text content of the element.
     * Updates the size when the text changes.
     */
    var text: String = ""
        set(value) {
            field = value
            updateSize()
        }

    /**
     * Flag indicating whether to render the text with a shadow.
     */
    var shadow = false

    /**
     * The scale of the text.
     * Updates the size when the scale changes.
     */
    var scale = 1.0
        set(value) {
            field = value
            updateSize()
        }

    /**
     * The line spacing multiplier for the text.
     * Updates the size when the line spacing changes.
     */
    var lineSpacing = 1.0
        set(value) {
            field = value
            updateSize()
        }

    /**
     * Updates the size of the element based on the current text, scale, and line spacing.
     */
    private fun updateSize() {
        val textRenderer: TextRenderer? = HudEngine.clientApi.minecraft()?.textRenderer
        if (textRenderer != null) {
            val textWidth = textRenderer.getWidth(Text.of(text.replace("\n", " "))) * scale
            val lineCount = text.split("\n").size
            val textHeight = (textRenderer.fontHeight * scale * lineCount) + ((lineCount - 1) * textRenderer.fontHeight * lineSpacing * scale)
            size = V3(textWidth, textHeight, size.z)
        }
    }

    /**
     * Renders the text with optional color support.
     *
     * @param drawContext The context used for drawing.
     * @param tickDelta The delta time between ticks.
     * @param colorParser A function to parse text into colored segments, or null for no color parsing.
     */
    protected fun renderText(drawContext: DrawContext, tickDelta: Float, colorParser: ((String) -> List<Pair<Color, String>>)?) {
        val textRenderer: TextRenderer? = HudEngine.clientApi.minecraft()?.textRenderer
        if (textRenderer != null) {
            drawContext.matrices.push()
            drawContext.matrices.scale(scale.toFloat(), scale.toFloat(), 1.0f)

            val lines = text.split("\n")
            var yOffset = 0

            lines.forEach { line ->
                var xOffset = 0
                val parts = colorParser?.invoke(line) ?: listOf(Color(255, 255, 255) to line)
                parts.forEach { part ->
                    val color = part.first
                    val textPart = part.second

                    drawContext.drawText(
                        textRenderer,
                        textPart,
                        (renderLocation.x / scale).toInt() + xOffset,
                        (renderLocation.y / scale).toInt() + yOffset,
                        color.toInt(),
                        shadow
                    )
                    xOffset += textRenderer.getWidth(Text.of(textPart))
                }
                yOffset += (textRenderer.fontHeight * (1 + lineSpacing) * scale).toInt()
            }

            drawContext.matrices.pop()
        }
    }
}