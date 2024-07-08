package ru.airdead.hudrenderer.element

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import ru.airdead.hudrenderer.HudEngine
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
     * The scale of the text.
     */
    var scale = 1.0
        set(value) {
            field = value
            updateSize()
        }

    /**
     * Updates the size of the text element based on its content and settings.
     */
    private fun updateSize() {
        val textRenderer: TextRenderer? = HudEngine.clientApi.minecraft()?.textRenderer
        val textWidth = (textRenderer?.getWidth(Text.of(content)) ?: 0) * scale
        val textHeight = (textRenderer?.fontHeight ?: 0) * scale
        size = V3(textWidth, textHeight, size.z)
    }

    /**
     * Renders the text element.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        val textRenderer: TextRenderer? = HudEngine.clientApi.minecraft()?.textRenderer
        if (textRenderer != null) {
            drawContext.matrices.push()
            drawContext.matrices.scale(scale.toFloat(), scale.toFloat(), 1.0f)
            drawContext.drawText(textRenderer, content, (renderLocation.x / scale).toInt(), (renderLocation.y / scale).toInt(), color.toInt(), shadow)
            drawContext.matrices.pop()
        }
    }
}
