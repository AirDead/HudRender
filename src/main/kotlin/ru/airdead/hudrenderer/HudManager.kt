package ru.airdead.hudrenderer

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.element.Parent
import ru.airdead.hudrenderer.event.MouseHoverMoveEvent
import ru.airdead.hudrenderer.utility.ClickContext
import ru.airdead.hudrenderer.utility.MouseButton

/**
 * The `HudManager` object is responsible for managing HUD elements, including their rendering and updates based on mouse interactions.
 */
object HudManager {

    private val elements = mutableListOf<AbstractElement>()
    private var previousLeftClickState = false
    private var previousRightClickState = false
    private var hoveredElement: AbstractElement? = null

    /**
     * Adds an element to the HUD.
     *
     * @param element The element to be added.
     */
    @JvmStatic
    fun addElement(element: AbstractElement) = elements.add(element)

    /**
     * Removes an element from the HUD.
     *
     * @param element The element to be removed.
     */
    @JvmStatic
    fun removeElement(element: AbstractElement) = elements.remove(element)

    /**
     * Updates the state of all HUD elements based on the current mouse position and click states.
     *
     * @param mouseX The current X position of the mouse.
     * @param mouseY The current Y position of the mouse.
     * @param isLeftClicked Whether the left mouse button is currently clicked.
     * @param isRightClicked Whether the right mouse button is currently clicked.
     */
    @JvmStatic
    fun update(mouseX: Double, mouseY: Double, isLeftClicked: Boolean, isRightClicked: Boolean) {
        elements.forEach { updateElement(it, mouseX, mouseY, isLeftClicked, isRightClicked) }
        previousLeftClickState = isLeftClicked
        previousRightClickState = isRightClicked
    }

    /**
     * Updates the state of a single HUD element based on the current mouse position and click states.
     *
     * @param element The element to be updated.
     * @param mouseX The current X position of the mouse.
     * @param mouseY The current Y position of the mouse.
     * @param isLeftClicked Whether the left mouse button is currently clicked.
     * @param isRightClicked Whether the right mouse button is currently clicked.
     */
    private fun updateElement(element: AbstractElement, mouseX: Double, mouseY: Double, isLeftClicked: Boolean, isRightClicked: Boolean) {
        element.apply {
            handleMouseHover(mouseX, mouseY)

            if (wasHovered) {
                HudEngine.clientApi.triggerEvent(
                    MouseHoverMoveEvent(MinecraftClient.getInstance(), mouseX, mouseY, this)
                )
                hoveredElement = this
            }
            if (isLeftClicked && !previousLeftClickState) {
                handleMouseClick(MouseButton.LEFT, ClickContext(true, mouseX, mouseY / 2))
            } else if (!isLeftClicked && previousLeftClickState) {
                handleMouseClick(MouseButton.LEFT, ClickContext(false, mouseX, mouseY / 2))
            }

            if (isRightClicked && !previousRightClickState) {
                handleMouseClick(MouseButton.RIGHT, ClickContext(true, mouseX, mouseY / 2))
            } else if (!isRightClicked && previousRightClickState) {
                handleMouseClick(MouseButton.RIGHT, ClickContext(false, mouseX, mouseY / 2))
            }

            if (this is Parent) children.forEach { updateElement(it, mouseX, mouseY, isLeftClicked, isRightClicked) }
        }
    }

    /**
     * Renders all HUD elements.
     *
     * @param drawContext The context used for drawing.
     * @param tickDelta The delta time since the last tick.
     */
    fun render(drawContext: DrawContext, tickDelta: Float) = elements.forEach { it.transformAndRender(drawContext, tickDelta) }

    /**
     * Returns the list of all HUD elements.
     *
     * @return A mutable list of all HUD elements.
     */
    fun elements(): MutableList<AbstractElement> = elements
}