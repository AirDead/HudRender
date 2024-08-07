package ru.airdead.hudrenderer.element

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import ru.airdead.hudrenderer.HudEngine
import ru.airdead.hudrenderer.utility.Color
import ru.airdead.hudrenderer.utility.V3

/**
 * A class representing a text element with color support for rendering in the HUD.
 */
class ColorTextElement : AbstractElement() {

    /**
     * The text to be displayed.
     * When set, it updates the size of the element.
     */
    var text: String = ""
        set(value) {
            field = value
            updateSize()
        }

    /**
     * Whether the text should have a shadow.
     */
    var shadow = false

    /**
     * The scale of the text.
     * When set, it updates the size of the element.
     */
    var scale = 1.0
        set(value) {
            field = value
            updateSize()
        }

    /**
     * A map of color codes to their corresponding Color objects.
     */
    private val colorCodes = mapOf(
        '0' to Color(0, 0, 0),
        '1' to Color(0, 0, 170),
        '2' to Color(0, 170, 0),
        '3' to Color(0, 170, 170),
        '4' to Color(170, 0, 0),
        '5' to Color(170, 0, 170),
        '6' to Color(255, 170, 0),
        '7' to Color(170, 170, 170),
        '8' to Color(85, 85, 85),
        '9' to Color(85, 85, 255),
        'a' to Color(85, 255, 85),
        'b' to Color(85, 255, 255),
        'c' to Color(255, 85, 85),
        'd' to Color(255, 85, 255),
        'e' to Color(255, 255, 85),
        'f' to Color(255, 255, 255)
    )

    /**
     * Updates the size of the element based on the current text and scale.
     */
    private fun updateSize() {
        val textRenderer: TextRenderer? = HudEngine.clientApi.minecraft()?.textRenderer
        val textWidth = (textRenderer?.getWidth(Text.of(text)) ?: 0) * scale
        val textHeight = (textRenderer?.fontHeight ?: 0) * scale
        size = V3(textWidth, textHeight, size.z)
    }

    /**
     * Renders the text element on the screen.
     *
     * @param drawContext The context used for drawing.
     * @param tickDelta The delta time between ticks.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        val textRenderer: TextRenderer? = HudEngine.clientApi.minecraft()?.textRenderer
        if (textRenderer != null) {
            drawContext.matrices.push()
            drawContext.matrices.scale(scale.toFloat(), scale.toFloat(), 1.0f)

            val parts = parseColor(text)
            var xOffset = 0
            for (part in parts) {
                val color = part.first
                val textPart = part.second
                drawContext.drawText(textRenderer, textPart, (renderLocation.x / scale).toInt() + xOffset, (renderLocation.y / scale).toInt(), color.toInt(), shadow)
                xOffset += textRenderer.getWidth(Text.of(textPart))
            }

            drawContext.matrices.pop()
        }
    }

    /**
     * Parses the text and returns a list of pairs containing the color and the corresponding text part.
     *
     * @param text The text to be parsed.
     * @return A list of pairs where each pair contains a Color and a String.
     */
    fun parseColor(text: String): List<Pair<Color, String>> {
        val result = mutableListOf<Pair<Color, String>>()
        var currentColor = colorCodes['f'] ?: Color(255, 255, 255)
        var buffer = StringBuilder()

        var i = 0
        while (i < text.length) {
            if (text[i] == '&' && i + 1 < text.length) {
                if (buffer.isNotEmpty()) {
                    result.add(currentColor.copy() to buffer.toString())
                    buffer = StringBuilder()
                }
                currentColor = colorCodes[text[i + 1]] ?: currentColor
                i++
            } else {
                buffer.append(text[i])
            }
            i++
        }

        if (buffer.isNotEmpty()) {
            result.add(currentColor to buffer.toString())
        }

        return result
    }
}