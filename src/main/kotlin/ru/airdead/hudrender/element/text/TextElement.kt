package ru.airdead.hudrender.element.text

import net.minecraft.client.gui.DrawContext

/**
 * A simple text element for rendering in the HUD without color support.
 */
class TextElement : BaseTextElement() {

    /**
     * Renders the text element without color parsing.
     *
     * @param drawContext The context used for drawing.
     * @param tickDelta The delta time between ticks.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        renderText(drawContext, tickDelta, null)
    }
}