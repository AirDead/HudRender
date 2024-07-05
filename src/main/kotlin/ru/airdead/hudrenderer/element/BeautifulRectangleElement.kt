package ru.airdead.hudrenderer.element

import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import ru.airdead.hudrenderer.utility.V3

/**
 * Class representing a rectangle element with rounded corners and optional texture.
 * Inherits from [AbstractElement] and implements the [Parent] interface.
 */
open class BeautifulRectangleElement : AbstractElement(), Parent {

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
     * The radius of the rounded corners.
     */
    var cornerRadius: Int = 8

    /**
     * Renders the rectangle element.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        val x1 = renderLocation.x.toInt()
        val y1 = renderLocation.y.toInt()
        val width = size.x.toInt().coerceAtLeast(0)
        val height = size.y.toInt().coerceAtLeast(0)

        if (width <= 0 || height <= 0) return

        drawRoundedRect(drawContext, x1, y1, width, height, cornerRadius, color.toInt())

        texture?.let {
            drawContext.drawTexture(
                it,
                x1, y1, width, height,
                0.0f, 0.0f,
                regionSize.x.toInt(), regionSize.y.toInt(),
                textureSize.x.toInt(), textureSize.y.toInt()
            )
        }

        children.forEach {
            it.transformAndRender(drawContext, tickDelta)
        }
    }

    /**
     * Draws a rectangle with rounded corners.
     *
     * @param drawContext The draw context used for rendering.
     * @param x The x-coordinate of the rectangle.
     * @param y The y-coordinate of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @param radius The radius of the rounded corners.
     * @param color The color of the rectangle.
     */
    private fun drawRoundedRect(drawContext: DrawContext, x: Int, y: Int, width: Int, height: Int, radius: Int, color: Int) {
        val adjustedRadius = radius.coerceAtMost(width / 2).coerceAtMost(height / 2)

        drawContext.fill(x + adjustedRadius, y + adjustedRadius, x + width - adjustedRadius, y + height - adjustedRadius, color)

        drawContext.fill(x + adjustedRadius, y, x + width - adjustedRadius, y + adjustedRadius, color)
        drawContext.fill(x + adjustedRadius, y + height - adjustedRadius, x + width - adjustedRadius, y + height, color)
        drawContext.fill(x, y + adjustedRadius, x + adjustedRadius, y + height - adjustedRadius, color)
        drawContext.fill(x + width - adjustedRadius, y + adjustedRadius, x + width, y + height - adjustedRadius, color)

        if (adjustedRadius > 0) {
            drawCorner(drawContext, x + adjustedRadius, y + adjustedRadius, adjustedRadius, 0, color)
            drawCorner(drawContext, x + width - adjustedRadius, y + adjustedRadius, adjustedRadius, 90, color)
            drawCorner(drawContext, x + width - adjustedRadius, y + height - adjustedRadius, adjustedRadius, 180, color)
            drawCorner(drawContext, x + adjustedRadius, y + height - adjustedRadius, adjustedRadius, 270, color)
        }
    }

    /**
     * Draws a corner of a rounded rectangle.
     *
     * @param drawContext The draw context used for rendering.
     * @param x The x-coordinate of the corner's center.
     * @param y The y-coordinate of the corner's center.
     * @param radius The radius of the corner.
     * @param startAngle The starting angle of the corner.
     * @param color The color of the corner.
     */
    private fun drawCorner(drawContext: DrawContext, x: Int, y: Int, radius: Int, startAngle: Int, color: Int) {
        val segments = 16
        val angleStep = Math.PI / 2 / segments

        var angle = startAngle * Math.PI / 180
        for (i in 0..segments) {
            val x1 = (x + radius * Math.cos(angle)).toInt()
            val y1 = (y + radius * Math.sin(angle)).toInt()
            val x2 = (x + radius * Math.cos(angle + angleStep)).toInt()
            val y2 = (y + radius * Math.sin(angle + angleStep)).toInt()

            drawContext.fill(x1, y1, x2, y2, color)

            angle += angleStep
        }
    }
}
