package ru.airdead.hudrenderer.element.rectangle

import net.minecraft.client.gui.DrawContext

/**
 * Class representing a simple rectangle element without masking.
 */
class NiceRectangleElement : BaseRectangleElement() {

    /**
     * Renders the rectangle content without masking.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    override fun renderContent(drawContext: DrawContext, tickDelta: Float) {
        renderChildren(drawContext, tickDelta)
    }

    /**
     * No children for NiceRectangleElement, so this is empty.
     */
    override fun renderChildren(drawContext: DrawContext, tickDelta: Float) {
        // No children to render for NiceRectangleElement
    }
}
