package ru.airdead.hudrender.element

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.GameMenuScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.util.math.RotationAxis
import ru.airdead.hudrender.HudEngine
import ru.airdead.hudrender.animation.*
import ru.airdead.hudrender.utility.*

/**
 * Abstract base class for HUD elements.
 * Provides common properties and methods for rendering, interaction, and animation.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class AbstractElement : IElement {

    /**
     * Indicates whether the element is enabled.
     * When set to false, the element becomes non-interactable.
     */
    open var enabled = true
        set(value) {
            field = value
            interactable = if (value) wasInteractable else false
        }

    private var wasInteractable = true
    var lastParent: AbstractElement? = null

    /**
     * The render location of the element.
     */
    var renderLocation = V3(0.0, 0.0, 0.0)
        private set

    /**
     * The size of the element.
     */
    override var size = V3(0.0, 0.0, 0.0)

    /**
     * The color of the element.
     */
    open var color = Color.BLACK

    /**
     * The alignment of the element.
     */
    var align = V3(0.0, 0.0)

    /**
     * The origin point of the element.
     */
    var origin = V3(0.0, 0.0)

    /**
     * The offset of the element.
     */
    var offset = V3(0.0, 0.0)

    /**
     * The rotation of the element.
     */
    var rotation = Rotation(0.0f)

    /**
     * Indicates whether the element is interactable.
     * The element must be enabled and interaction must be allowed.
     */
    open var interactable = true
        get() = field && isInteractionAllowed()
        set(value) {
            field = value
            if (!enabled) wasInteractable = value
        }

    private var onLeftClick: ClickHandler? = null
    private var onRightClick: ClickHandler? = null
    private var onHover: HoverHandler? = null

    /**
     * Indicates whether the element was hovered over.
     */
    var wasHovered = false
        private set

    private val animations = mutableListOf<AnimationChain>()

    /**
     * Sets the hover handler for the element.
     *
     * @param handler The hover handler.
     */
    @ElementBuilderDsl
    fun onHover(handler: HoverHandler) { onHover = handler }

    /**
     * Sets the left-click handler for the element.
     *
     * @param handler The left-click handler.
     */
    @ElementBuilderDsl
    fun onLeftClick(handler: ClickHandler) { onLeftClick = handler }

    /**
     * Sets the right-click handler for the element.
     *
     * @param handler The right-click handler.
     */
    @ElementBuilderDsl
    fun onRightClick(handler: ClickHandler) { onRightClick = handler }

    /**
     * Transforms and renders the element.
     *
     * @param drawContext The draw context.
     * @param tickDelta The time delta since the last update.
     */
    fun transformAndRender(drawContext: DrawContext, tickDelta: Float) {
        if (!enabled) return
        updateAnimations(tickDelta)
        updateRenderLocation(drawContext)
        handleRotation(drawContext, true)
        drawContext.matrices.push()
        drawContext.matrices.translate(0.0, offset.y, 0.0)
        render(drawContext, tickDelta)
        drawContext.matrices.pop()
        handleRotation(drawContext, false)
    }

    /**
     * Updates the render location of the element.
     *
     * @param drawContext The draw context.
     */
    private fun updateRenderLocation(drawContext: DrawContext) {
        val defaultSize = V3(drawContext.scaledWindowWidth.toDouble(), drawContext.scaledWindowHeight.toDouble(), 1.0)
        val parentSize = lastParent?.size ?: defaultSize
        renderLocation = V3(
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
     * @return The absolute position of the element.
     */
    open fun calculateAbsolutePosition(parentSize: Double, size: Double, align: Double, origin: Double, parentOffset: Double?, offset: Double) =
        parentSize * align - size * origin + (parentOffset ?: 0.0) + offset

    /**
     * Handles mouse click events.
     *
     * @param button The mouse button that was clicked.
     * @param context The click context.
     */
    open fun handleMouseClick(button: MouseButton, context: ClickContext) {
        if (interactable && wasHovered) {
            when (button) {
                MouseButton.LEFT -> onLeftClick?.invoke(context)
                MouseButton.RIGHT -> onRightClick?.invoke(context)
                else -> return
            }
        }
    }

    /**
     * Handles the rotation of the element.
     *
     * @param drawContext The draw context.
     * @param preRender Indicates whether the rotation is applied before rendering.
     */
    private fun handleRotation(drawContext: DrawContext, preRender: Boolean) {
        if (rotation.degrees == 0.0f) return
        val originOffsetX = size.x * origin.x
        val originOffsetY = size.y * origin.y
        val rotateX = renderLocation.x + originOffsetX
        val rotateY = renderLocation.y + originOffsetY
        val matrix = drawContext.matrices

        if (preRender) {
            matrix.push()
            matrix.translate(rotateX, rotateY, 0.0)
            matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation.degrees))
            matrix.translate(-rotateX, -rotateY, 0.0)
        } else {
            matrix.pop()
        }
    }

    /**
     * Checks if the element is hovered over.
     *
     * @param mouseX The X coordinate of the mouse.
     * @param mouseY The Y coordinate of the mouse.
     * @return True if the element is hovered over, false otherwise.
     */
    open fun isHovered(mouseX: Double, mouseY: Double) = mouseX in renderLocation.x..(renderLocation.x + size.x) &&
            mouseY in renderLocation.y..(renderLocation.y + size.y)

    /**
     * Handles mouse hover events.
     *
     * @param mouseX The X coordinate of the mouse.
     * @param mouseY The Y coordinate of the mouse.
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
     * Creates an animation for the element.
     *
     * @param duration The duration of the animation.
     * @param easing The easing function for the animation.
     * @param updateProperties A lambda to update the properties of the element.
     * @return The animation chain.
     */
    fun animate(duration: Double, easing: Easing = Easings.NONE, updateProperties: AbstractElement.() -> Unit): AnimationChain {
        val startState = copyProperties()
        updateProperties()
        val endState = copyProperties()
        val animation = Animation(startState, endState, duration * 20, easing::ease, ::applyProperties)
        val chain = AnimationChain(this, animation)
        animations.add(chain)
        return chain
    }

    /**
     * Updates the animations of the element.
     *
     * @param tickDelta The time delta since the last update.
     */
    open fun updateAnimations(tickDelta: Float) {
        animations.removeIf { it.update(tickDelta) }
    }

    /**
     * Checks if interaction is allowed for the element.
     *
     * @return True if interaction is allowed, false otherwise.
     */
    open fun isInteractionAllowed() = HudEngine.clientApi.minecraft()?.currentScreen !is GameMenuScreen &&
            HudEngine.clientApi.minecraft()?.currentScreen !is InventoryScreen

    /**
     * Renders the element.
     *
     * @param drawContext The draw context.
     * @param tickDelta The time delta since the last update.
     */
    abstract fun render(drawContext: DrawContext, tickDelta: Float)
}