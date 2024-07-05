package ru.airdead.hudrenderer.element

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.option.ChatVisibility
import ru.airdead.hudrenderer.utility.*
import ru.airdead.hudrenderer.UIEngine
import ru.airdead.hudrenderer.UIEngine.clientApi
import ru.airdead.hudrenderer.stuff.KostilScreen
import ru.airdead.hudrenderer.utility.Color
import ru.airdead.hudrenderer.utility.ElementBuilderDSL
import ru.airdead.hudrenderer.utility.ScrollContext
import ru.airdead.hudrenderer.utility.ScrollHandler

typealias DragHandler = (DragContext) -> Unit

/**
 * Class representing a context menu.
 * Inherits from [AbstractElement] and implements the [Parent] interface.
 */
class ContextMenu : AbstractElement(), Parent {

    /**
     * Indicates whether the context menu is enabled.
     */
    override var enabled = false

    /**
     * Indicates whether the context menu is interactable.
     */
    override var interactable = false

    /**
     * The color of the context menu.
     */
    override var color = Color(0, 0, 0, 0.8)

    /**
     * The list of child elements.
     */
    override val children = mutableListOf<AbstractElement>()

    private var onKeyPressed: ButtonHandler? = null
    private var onScroll: ScrollHandler? = null
    private var onDrag: DragHandler? = null
    private var draggingElement: AbstractElement? = null

    /**
     * Renders the context menu.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    override fun render(drawContext: DrawContext, tickDelta: Float) {
        drawContext.fill(0, 0, drawContext.scaledWindowWidth, drawContext.scaledWindowHeight, color.toInt())
        children.forEach { it.transformAndRender(drawContext, tickDelta) }
    }

    /**
     * Shows the context menu and hides the chat.
     */
    fun show() {
        enabled = true
        MinecraftClient.getInstance().execute {
            clientApi.minecraft().apply {
                setScreen(KostilScreen())
                options.chatVisibility.value = ChatVisibility.HIDDEN
            }
        }
        UIEngine.isHudHide = true
    }

    /**
     * Hides the context menu and restores the chat visibility.
     */
    fun hide() {
        enabled = false
        clientApi.minecraft().options.chatVisibility.value = ChatVisibility.FULL
        UIEngine.isHudHide = false
    }

    /**
     * Sets a handler for key press events.
     *
     * @param action The handler to be invoked on key press.
     */
    @ElementBuilderDSL
    fun onKeyPressed(action: ButtonHandler) {
        onKeyPressed = action
    }

    /**
     * Sets a handler for scroll events.
     *
     * @param handler The handler to be invoked on scroll.
     */
    @ElementBuilderDSL
    fun onScroll(handler: ScrollHandler) {
        onScroll = handler
    }

    /**
     * Sets a handler for drag events.
     *
     * @param handler The handler to be invoked on drag.
     */
    @ElementBuilderDSL
    fun onDrag(handler: DragHandler) {
        onDrag = handler
    }

    /**
     * Handles key press events.
     *
     * @param keyCode The key code of the pressed key.
     * @param modifiers The set of active modifiers.
     */
    fun handleKeyPressed(keyCode: Int, modifiers: Set<Modifiers>) {
        val context = ButtonContext(keyCode, modifiers)
        onKeyPressed?.invoke(context)
    }

    /**
     * Handles scroll events.
     *
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param amount The amount of scroll.
     */
    fun handleScroll(mouseX: Double, mouseY: Double, amount: Double) {
        val hoveredElement = children.find { it.isHovered(mouseX, mouseY) }
        val context = ScrollContext(hoveredElement, amount)
        onScroll?.invoke(context)
    }

    /**
     * Handles drag events.
     *
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param dx The change in x-coordinate during the drag.
     * @param dy The change in y-coordinate during the drag.
     */
    fun handleDrag(mouseX: Double, mouseY: Double, dx: Double, dy: Double) {
        if (draggingElement == null) {
            draggingElement = children.find { it.isHovered(mouseX, mouseY) }
        }
        draggingElement?.let {
            val context = DragContext(it, mouseX, mouseY, dx, dy)
            onDrag?.invoke(context)
        }
    }
}

/**
 * Data class representing the context of a drag event.
 *
 * @param element The element being dragged.
 * @param mouseX The x-coordinate of the mouse.
 * @param mouseY The y-coordinate of the mouse.
 * @param dx The change in x-coordinate during the drag.
 * @param dy The change in y-coordinate during the drag.
 */
data class DragContext(
    val element: AbstractElement,
    val mouseX: Double,
    val mouseY: Double,
    val dx: Double,
    val dy: Double
)
