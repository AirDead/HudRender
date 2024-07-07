package ru.airdead.hudrenderer.element

import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import ru.airdead.hudrenderer.utility.V3

/**
 * Class representing a rectangle element.
 * Inherits from [AbstractElement] and implements the [Parent] interface.
 */
open class RectangleElement : AbstractElement(), Parent {

    /**
     * The list of child elements.
     */
    override val children: MutableList<AbstractElement> = mutableListOf()

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
     * Indicates whether masking is enabled.
     */
    var mask: Boolean = false

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
            drawContext.drawTexture(it, x1, y1, width, height, 0.0f, 0.0f,
                regionSize.x.toInt(), regionSize.y.toInt(), textureSize.x.toInt(), textureSize.y.toInt())
        } ?: drawContext.fill(x1, y1, x1 + width, y1 + height, color.toInt())

        if (mask) {
            drawContext.enableScissor(x1, y1, width, height)
        }

        children.forEach { it.transformAndRender(drawContext, tickDelta) }

        if (mask) {
            drawContext.disableScissor()
        }
    }
}
