package ru.airdead.hudrenderer

import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.element.Parent
import ru.airdead.hudrenderer.utility.MouseButton

/**
 * Singleton object managing UI elements.
 */
object HudManager {

    /**
     * List of all UI elements.
     */
    private val elements = mutableListOf<AbstractElement>()

    /**
     * Adds an element to the UI manager.
     *
     * @param element The element to be added.
     */
    @JvmStatic
    fun addElement(element: AbstractElement) {
        elements.add(element)
    }

    /**
     * Removes an element from the UI manager.
     *
     * @param element The element to be removed.
     */
    @JvmStatic
    fun removeElement(element: AbstractElement) {
        elements.remove(element)
    }

    /**
     * Updates the state of the UI elements based on mouse input.
     *
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param isLeftTurn Indicates if the left mouse button is clicked.
     * @param isRightTurn Indicates if the right mouse button is clicked.
     */
    @JvmStatic
    fun update(mouseX: Double, mouseY: Double, isLeftTurn: Boolean, isRightTurn: Boolean) {
        elements.forEach { element ->
            updateElement(element, mouseX, mouseY, isLeftTurn, isRightTurn)
        }
    }

    /**
     * Updates an individual element based on mouse input.
     *
     * @param element The element to be updated.
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param isLeftTurn Indicates if the left mouse button is clicked.
     * @param isRightTurn Indicates if the right mouse button is clicked.
     */
    private fun updateElement(element: AbstractElement, mouseX: Double, mouseY: Double, isLeftTurn: Boolean, isRightTurn: Boolean) {
        element.apply {
            handleMouseHover(mouseX, mouseY)
            handleMouseClick(MouseButton.LEFT, isLeftTurn)
            handleMouseClick(MouseButton.RIGHT, isRightTurn)

            if (this is Parent) {
                children.forEach { child ->
                    updateElement(child, mouseX, mouseY, isLeftTurn, isRightTurn)
                }
            }
        }
    }

    /**
     * Renders all UI elements.
     *
     * @param drawContext The draw context used for rendering.
     * @param tickDelta The delta time since the last tick.
     */
    fun render(drawContext: DrawContext, tickDelta: Float) {
        elements.forEach { it.transformAndRender(drawContext, tickDelta) }
    }

    /**
     * Returns the list of all UI elements.
     *
     * @return The list of UI elements.
     */
    fun elements(): MutableList<AbstractElement> {
        return elements
    }
}