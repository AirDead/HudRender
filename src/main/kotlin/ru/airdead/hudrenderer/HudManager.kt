package ru.airdead.hudrenderer

import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.element.Parent
import ru.airdead.hudrenderer.utility.ClickContext
import ru.airdead.hudrenderer.utility.MouseButton

object HudManager {

    private val elements = mutableListOf<AbstractElement>()
    private var previousLeftClickState = false
    private var previousRightClickState = false

    @JvmStatic
    fun addElement(element: AbstractElement) = elements.add(element)

    @JvmStatic
    fun removeElement(element: AbstractElement) = elements.remove(element)

    @JvmStatic
    fun update(mouseX: Double, mouseY: Double, isLeftClicked: Boolean, isRightClicked: Boolean) {
        elements.forEach { updateElement(it, mouseX, mouseY, isLeftClicked, isRightClicked) }
        previousLeftClickState = isLeftClicked
        previousRightClickState = isRightClicked
    }

    private fun updateElement(element: AbstractElement, mouseX: Double, mouseY: Double, isLeftClicked: Boolean, isRightClicked: Boolean) {
        element.apply {
            handleMouseHover(mouseX, mouseY)

            if (isLeftClicked && !previousLeftClickState) {
                handleMouseClick(MouseButton.LEFT, ClickContext(true))
            } else if (!isLeftClicked && previousLeftClickState) {
                handleMouseClick(MouseButton.LEFT, ClickContext(false))
            }

            if (isRightClicked && !previousRightClickState) {
                handleMouseClick(MouseButton.RIGHT, ClickContext(true))
            } else if (!isRightClicked && previousRightClickState) {
                handleMouseClick(MouseButton.RIGHT, ClickContext(false))
            }

            if (this is Parent) children.forEach { updateElement(it, mouseX, mouseY, isLeftClicked, isRightClicked) }
        }
    }

    fun render(drawContext: DrawContext, tickDelta: Float) = elements.forEach { it.transformAndRender(drawContext, tickDelta) }
    fun elements(): MutableList<AbstractElement> = elements
}
