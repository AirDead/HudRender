package ru.airdead.hudrenderer.element.rectangle

import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.element.Parent

/**
 * Class representing a rectangle element that can contain children and supports masking.
 * Inherits from [BaseRectangleElement] and implements the [Parent] interface.
 */
class RectangleElement : BaseRectangleElement(), Parent {

    /**
     * The list of child elements.
     */
    override val children: MutableList<AbstractElement> = mutableListOf()

    /**
     * Indicates whether masking is enabled.
     */
    var mask: Boolean = false

    /**
     * Renders the rectangle content with optional masking.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    override fun renderContent(drawContext: DrawContext, tickDelta: Float) {
        val (x1, y1) = renderLocation.run { x.toInt() to y.toInt() }
        val (width, height) = size.run { x.toInt() to y.toInt() }

        if (mask) {
            drawContext.enableScissor(x1, y1, width, height)
        }

        renderChildren(drawContext, tickDelta)

        if (mask) {
            drawContext.disableScissor()
        }
    }

    /**
     * Renders the child elements.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    override fun renderChildren(drawContext: DrawContext, tickDelta: Float) {
        children.forEach { it.transformAndRender(drawContext, tickDelta) }
    }
}