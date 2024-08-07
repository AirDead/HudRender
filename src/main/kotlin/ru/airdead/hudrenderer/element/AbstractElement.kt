package ru.airdead.hudrenderer.element

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.GameMenuScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.util.math.RotationAxis
import ru.airdead.hudrenderer.HudEngine
import ru.airdead.hudrenderer.utility.*

/**
 * Abstract base class representing a UI element in the UIEngine.
 * Implements the [IElement] interface.
 */
abstract class AbstractElement : IElement {

    /**
     * Indicates whether the element is enabled.
     * If disabled, the element will not be interactable.
     */
    open var enabled = true
        set(value) {
            field = value
            if (!value) {
                wasInteractable = interactable
                interactable = false
            } else {
                interactable = wasInteractable
            }
        }

    private var wasInteractable = true

    /**
     * The last parent element of this element.
     */
    var lastParent: AbstractElement? = null

    /**
     * The render location of this element in 3D space.
     */
    var renderLocation = V3(0.0, 0.0, 0.0)
        private set

    /**
     * The size of this element.
     */
    override var size = V3(0.0, 0.0, 0.0)

    /**
     * The color of this element.
     */
    open var color = BLACK

    /**
     * The alignment of this element.
     */
    var align = TOP_LEFT

    /**
     * The origin point of this element.
     */
    var origin = TOP_LEFT

    /**
     * The offset of this element from its original position.
     */
    var offset = V3(0.0, 0.0)

    /**
     * The rotation of this element.
     */
    var rotation = Rotation(0.0f)

    /**
     * Indicates whether the element is interactable.
     * If the element is disabled, it cannot be interactable.
     */
    open var interactable = true
        get() = field && isInteractionAllowed()
        set(value) {
            field = value
            if (!enabled) {
                wasInteractable = value
            }
        }

    private var onLeftClick: ClickHandler? = null
    private var onRightClick: ClickHandler? = null
    private var onHover: HoverHandler? = null

    /**
     * Indicates whether the element was hovered in the last frame.
     */
    var wasHovered = false
        private set

    /**
     * Sets a handler for hover events.
     *
     * @param handler The handler to be invoked on hover.
     */
    @ElementBuilderDsl
    fun onHover(handler: HoverHandler) {
        onHover = handler
    }

    /**
     * Sets a handler for left click events.
     *
     * @param handler The handler to be invoked on left click.
     */
    @ElementBuilderDsl
    fun onLeftClick(handler: ClickHandler) {
        onLeftClick = handler
    }

    /**
     * Sets a handler for right click events.
     *
     * @param handler The handler to be invoked on right click.
     */
    @ElementBuilderDsl
    fun onRightClick(handler: ClickHandler) {
        onRightClick = handler
    }

    /**
     * Transforms and renders the element.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    fun transformAndRender(drawContext: DrawContext, tickDelta: Float) {
        if (!enabled) return

        updateRenderLocation(drawContext)
        handleRotation(drawContext, true)

        drawContext.matrices.push()
        drawContext.matrices.translate(0.0, offset.y, 0.0)

        render(drawContext, tickDelta)

        drawContext.matrices.pop()
        handleRotation(drawContext, false)
    }

    /**
     * Updates the render location based on the parent size and offset.
     *
     * @param drawContext The draw context used for rendering.
     */
    private fun updateRenderLocation(drawContext: DrawContext) {
        val defaultSize = V3(
            drawContext.scaledWindowWidth.toDouble(),
            drawContext.scaledWindowHeight.toDouble(),
            1.0
        )

        val parentSize = when (lastParent) {
            null, is ContextMenu -> defaultSize
            else -> lastParent?.size ?: defaultSize
        }

        renderLocation = calculateRenderLocation(parentSize)
    }

    /**
     * Calculates the render location of the element based on the parent size.
     *
     * @param parentSize The size of the parent element.
     * @return The calculated render location.
     */
    private fun calculateRenderLocation(parentSize: V3): V3 {
        return V3(
            calculateAbsolutePosition(parentSize.x, size.x, align.x, origin.x, lastParent?.renderLocation?.x, offset.x),
            calculateAbsolutePosition(parentSize.y, size.y, align.y, origin.y, lastParent?.renderLocation?.y, offset.y),
            renderLocation.z
        )
    }

    /**
     * Calculates the absolute position of the element.
     *
     * @param parentSize The size of the parent element.
     * @param size The size of the element.
     * @param align The alignment of the element.
     * @param origin The origin point of the element.
     * @param parentOffset The offset of the parent element.
     * @param offset The offset of the element.
     * @return The calculated absolute position.
     */
    private fun calculateAbsolutePosition(
        parentSize: Double,
        size: Double,
        align: Double,
        origin: Double,
        parentOffset: Double?,
        offset: Double
    ): Double {
        return parentSize * align - size * origin + (parentOffset ?: 0.0) + offset
    }

    /**
     * Handles mouse click events.
     *
     * @param button The mouse button clicked.
     * @param context The context of the click.
     */
    open fun handleMouseClick(button: MouseButton, context: ClickContext) {
        if (interactable) {
            when (button) {
                MouseButton.LEFT -> if (wasHovered) onLeftClick?.invoke(context)
                MouseButton.RIGHT -> if (wasHovered) onRightClick?.invoke(context)
                else -> return
            }
        }
    }

    /**
     * Handles the rotation of the element during rendering.
     *
     * @param drawContext The draw context used for rendering.
     * @param preRender Indicates if the rotation is applied before rendering.
     */
    private fun handleRotation(drawContext: DrawContext, preRender: Boolean) {
        if (rotation.degrees == 0.0f) return

        val originOffsetX = size.x * origin.x
        val originOffsetY = size.y * origin.y
        val rotateX = renderLocation.x + originOffsetX
        val rotateY = renderLocation.y + originOffsetY

        if (preRender) {
            drawContext.matrices.push()
            drawContext.matrices.translate(rotateX, rotateY, 0.0)
            drawContext.matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation.degrees))
            drawContext.matrices.translate(-rotateX, -rotateY, 0.0)
        } else {
            drawContext.matrices.pop()
        }
    }

    /**
     * Checks if the element is hovered based on the mouse coordinates.
     *
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @return True if the element is hovered, false otherwise.
     */
    fun isHovered(mouseX: Double, mouseY: Double): Boolean {
        val xStart = renderLocation.x
        val xEnd = xStart + size.x
        val yStart = renderLocation.y
        val yEnd = yStart + size.y
        return mouseX in xStart..xEnd && mouseY in yStart..yEnd
    }

    /**
     * Handles mouse hover events.
     *
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     */
    open fun handleMouseHover(mouseX: Double, mouseY: Double) {
        if (!interactable) return
        val hovered = isHovered(mouseX, mouseY)
        if (hovered != wasHovered) {
            onHover?.invoke(HoverContext(hovered, mouseX, mouseY / 2))
            wasHovered = hovered
        }
    }

    /**
     * Checks if interaction is allowed based on the current screen.
     *
     * @return True if interaction is allowed, false otherwise.
     */
    private fun isInteractionAllowed(): Boolean {
        val currentScreen = HudEngine.clientApi.minecraft()?.currentScreen
        return currentScreen !is GameMenuScreen && currentScreen !is InventoryScreen
    }

    /**
     * Renders the element. Must be implemented by subclasses.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    abstract fun render(drawContext: DrawContext, tickDelta: Float)
}
