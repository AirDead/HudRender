package ru.airdead.hudrenderer.element.text

import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.utility.Color

/**
 * A text element with color support for rendering in the HUD.
 */
class ColorTextElement : BaseTextElement() {

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
     * Renders the text element with color parsing.
     *
     * @param drawContext The context used for drawing.
     * @param tickDelta The delta time between ticks.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        renderText(drawContext, tickDelta, this::parseColor)
    }

    /**
     * Parses the text into colored segments.
     *
     * @param text The text to parse.
     * @return A list of pairs where each pair contains a Color and a String.
     */
    private fun parseColor(text: String): List<Pair<Color, String>> {
        val result = mutableListOf<Pair<Color, String>>()
        var currentColor = colorCodes['f'] ?: Color(255, 255, 255)
        val buffer = StringBuilder()

        var i = 0
        while (i < text.length) {
            if (text[i] == '&' && i + 1 < text.length) {
                if (buffer.isNotEmpty()) {
                    result.add(currentColor.copy() to buffer.toString())
                    buffer.clear()
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