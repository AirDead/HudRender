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

    open var enabled = true
        set(value) {
            field = value
            interactable = if (value) wasInteractable else false
        }

    private var wasInteractable = true
    var lastParent: AbstractElement? = null

    var renderLocation = V3(0.0, 0.0, 0.0)
        private set

    override var size = V3(0.0, 0.0, 0.0)

    open var color = Color.BLACK

    var align = TOP_LEFT

    var origin = TOP_LEFT

    var offset = V3(0.0, 0.0)

    var rotation = Rotation(0.0f)

    open var interactable = true
        get() = field && isInteractionAllowed()
        set(value) {
            field = value
            if (!enabled) wasInteractable = value
        }

    private var onLeftClick: ClickHandler? = null
    private var onRightClick: ClickHandler? = null
    private var onHover: HoverHandler? = null

    var wasHovered = false
        private set

    private val animations = mutableListOf<AnimationChain>()

    @ElementBuilderDsl
    fun onHover(handler: HoverHandler) { onHover = handler }

    @ElementBuilderDsl
    fun onLeftClick(handler: ClickHandler) { onLeftClick = handler }

    @ElementBuilderDsl
    fun onRightClick(handler: ClickHandler) { onRightClick = handler }

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

    private fun updateRenderLocation(drawContext: DrawContext) {
        val defaultSize = V3(drawContext.scaledWindowWidth.toDouble(), drawContext.scaledWindowHeight.toDouble(), 1.0)
        val parentSize = lastParent?.size ?: defaultSize
        renderLocation = V3(
            calculateAbsolutePosition(parentSize.x, size.x, align.x, origin.x, lastParent?.renderLocation?.x, offset.x),
            calculateAbsolutePosition(parentSize.y, size.y, align.y, origin.y, lastParent?.renderLocation?.y, offset.y),
            renderLocation.z
        )
    }

    open fun calculateAbsolutePosition(parentSize: Double, size: Double, align: Double, origin: Double, parentOffset: Double?, offset: Double) =
        parentSize * align - size * origin + (parentOffset ?: 0.0) + offset

    open fun handleMouseClick(button: MouseButton, context: ClickContext) {
        if (interactable && wasHovered) {
            when (button) {
                MouseButton.LEFT -> onLeftClick?.invoke(context)
                MouseButton.RIGHT -> onRightClick?.invoke(context)
                else -> return
            }
        }
    }

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

    open fun isHovered(mouseX: Double, mouseY: Double) = mouseX in renderLocation.x..(renderLocation.x + size.x) &&
            mouseY in renderLocation.y..(renderLocation.y + size.y)

    open fun handleMouseHover(mouseX: Double, mouseY: Double) {
        if (!interactable) return
        val hovered = isHovered(mouseX, mouseY)
        if (hovered != wasHovered) {
            onHover?.invoke(HoverContext(hovered, mouseX, mouseY / 2))
            wasHovered = hovered
        }
    }

    fun animate(duration: Double, easing: Easing = Easings.NONE, updateProperties: AbstractElement.() -> Unit): AnimationChain {
        val startState = copyProperties()
        updateProperties()
        val endState = copyProperties()
        val animation = Animation(startState, endState, duration * 20, easing::ease, ::applyProperties)
        val chain = AnimationChain(this, animation)
        animations.add(chain)
        return chain
    }

    open fun updateAnimations(tickDelta: Float) {
        animations.removeIf { it.update(tickDelta) }
    }

    open fun isInteractionAllowed() = HudEngine.clientApi.minecraft()?.currentScreen !is GameMenuScreen &&
            HudEngine.clientApi.minecraft()?.currentScreen !is InventoryScreen

    abstract fun render(drawContext: DrawContext, tickDelta: Float)
}
