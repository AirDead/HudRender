package ru.airdead.hudrenderer.element

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.option.ChatVisibility
import ru.airdead.hudrenderer.HudEngine
import ru.airdead.hudrenderer.HudEngine.clientApi
import ru.airdead.hudrenderer.stuff.KostilScreen
import ru.airdead.hudrenderer.utility.*

/**
 * Class representing a context menu.
 * Inherits from [AbstractElement] and implements the [Parent] interface.
 */
class ContextMenu : AbstractElement(), Parent {

    override var enabled = false
    override var interactable = false
    override var color = Color(0, 0, 0, 0.8)
    override val children = mutableListOf<AbstractElement>()

    private var onKeyPressed: ButtonHandler? = null
    private var onScroll: ScrollHandler? = null
    private var onDrag: DragHandler? = null
    private var onClose: CloseHandler? = null
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
            clientApi.minecraft()?.apply {
                options?.chatVisibility?.value = ChatVisibility.HIDDEN
                setScreen(KostilScreen())
            }
        }
        HudEngine.isHudHide = true
    }

    /**
     * Hides the context menu and restores the chat visibility.
     */
    fun hide() {
        enabled = false
        clientApi.minecraft()?.options?.chatVisibility?.value = ChatVisibility.FULL
        HudEngine.isHudHide = false
        onClose?.invoke()
    }

    /**
     * Sets a handler for key press events.
     *
     * @param action The handler to be invoked on key press.
     */
    @ElementBuilderDsl
    fun onKeyPressed(action: ButtonHandler) {
        onKeyPressed = action
    }

    /**
     * Sets a handler for closing the menu.
     *
     * @param action The handler to be invoked on menu close.
     */
    @ElementBuilderDsl
    fun onMenuClose(action: CloseHandler) {
        onClose = action
    }

    /**
     * Sets a handler for scroll events.
     *
     * @param handler The handler to be invoked on scroll.
     */
    @ElementBuilderDsl
    fun onScroll(handler: ScrollHandler) {
        onScroll = handler
    }

    /**
     * Sets a handler for drag events.
     *
     * @param handler The handler to be invoked on drag.
     */
    @ElementBuilderDsl
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

typealias CloseHandler = () -> Unit