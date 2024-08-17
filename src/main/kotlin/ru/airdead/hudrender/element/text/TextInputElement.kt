package ru.airdead.hudrender.element.text

import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrender.HudEngine
import ru.airdead.hudrender.element.AbstractElement
import ru.airdead.hudrender.utility.ClickContext
import ru.airdead.hudrender.utility.Color
import ru.airdead.hudrender.utility.MouseButton
import ru.airdead.hudrender.utility.V3

/**
 * A class representing a text input field.
 * Inherits from [AbstractElement] and provides methods for handling text input.
 */
class TextInputElement : AbstractElement() {

    /**
     * The current text content of the input field.
     */
    var text: String = ""
        private set

    /**
     * The maximum length of the text that can be entered.
     */
    var maxLength: Int = 100

    /**
     * Indicates whether the input field is focused and ready to accept input.
     */
    var focused: Boolean = false

    /**
     * The color of the text.
     */
    var textColor: Color = Color.WHITE

    /**
     * The background color of the input field.
     */
    var backgroundColor: Color = Color(0, 0, 0, 0.5)


    /**
     * Handles key press events and updates the text content if the input field is focused.
     */
    fun handleKeyPress(char: Char, keyCode: Int) {
        if (!focused || text.length >= maxLength) return

        when (keyCode) {
            259 -> { // Backspace
                if (text.isNotEmpty()) {
                    text = text.substring(0, text.length - 1)
                }
            }
            257, 335 -> { // Enter key
                focused = false
            }
            else -> {
                if (char.isLetterOrDigit() || char.isWhitespace()) {
                    text += char
                }
            }
        }
    }

    /**
     * Renders the input field, including the text, background, and border.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        val (x, y) = renderLocation.run { x.toInt() to y.toInt() }
        val (width, height) = size.run { x.toInt() to y.toInt() }

        drawContext.fill(x, y, x + width, y + height, backgroundColor.toInt())

        val textRenderer = HudEngine.clientApi.minecraft()?.textRenderer ?: return
        val textY = y + (height - textRenderer.fontHeight) / 2
        drawContext.drawText(textRenderer, text, x + 5, textY, textColor.toInt(), false)
    }

    /**
     * Handles mouse click events to focus the input field when clicked.
     * Deactivates the input field when clicking outside of it.
     */
    override fun handleMouseClick(button: MouseButton, context: ClickContext) {
        super.handleMouseClick(button, context)
        focused = isHovered(context.mouseX, context.mouseY)
    }

    /**
     * Checks if the input field is hovered.
     */
    override fun isHovered(mouseX: Double, mouseY: Double): Boolean {
        return mouseX in renderLocation.x..(renderLocation.x + size.x) &&
                mouseY in renderLocation.y..(renderLocation.y + size.y)
    }
}
