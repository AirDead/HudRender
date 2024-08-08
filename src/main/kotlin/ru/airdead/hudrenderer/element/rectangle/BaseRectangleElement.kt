package ru.airdead.hudrenderer.element.rectangle

import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.utility.V3

/**
 * Base class for rectangle elements, handling common properties and rendering logic.
 */
abstract class BaseRectangleElement : AbstractElement() {

    /**
     * The texture to be applied to the rectangle.
     */
    var texture: Identifier? = null

    /**
     * The size of the region to be rendered from the texture.
     */
    var regionSize = V3(256.0, 256.0)

    /**
     * The actual size of the texture.
     */
    var textureSize = V3(256.0, 256.0)

    /**
     * Renders the rectangle element.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        val (x1, y1) = renderLocation.run { x.toInt() to y.toInt() }
        val (width, height) = size.run { x.toInt() to y.toInt() }

        if (width <= 0 || height <= 0) return

        texture?.let {
            drawContext.drawTexture(
                it, x1, y1, width, height, 0.0f, 0.0f,
                regionSize.x.toInt(), regionSize.y.toInt(), textureSize.x.toInt(), textureSize.y.toInt()
            )
        } ?: drawContext.fill(x1, y1, x1 + width, y1 + height, color.toInt())

        renderContent(drawContext, tickDelta)
    }

    /**
     * Renders the rectangle content.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    protected abstract fun renderContent(drawContext: DrawContext, tickDelta: Float)

    /**
     * Renders the child elements.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    abstract fun renderChildren(drawContext: DrawContext, tickDelta: Float)
}