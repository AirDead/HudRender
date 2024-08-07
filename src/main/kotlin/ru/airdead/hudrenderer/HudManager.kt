package ru.airdead.hudrenderer

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.element.Parent
import ru.airdead.hudrenderer.event.MouseHoverMoveEvent
import ru.airdead.hudrenderer.utility.ClickContext
import ru.airdead.hudrenderer.utility.MouseButton

object HudManager {

    private val elements = mutableListOf<AbstractElement>()
    private var previousLeftClickState = false
    private var previousRightClickState = false
    private var lastMouseX = Double.NaN
    private var lastMouseY = Double.NaN

    @JvmStatic
    fun addElement(element: AbstractElement) = elements.add(element)

    @JvmStatic
    fun removeElement(element: AbstractElement) = elements.remove(element)

    @JvmStatic
    fun update(mouseX: Double, mouseY: Double, isLeftClicked: Boolean, isRightClicked: Boolean) {
        elements.forEach { updateElement(it, mouseX, mouseY, isLeftClicked, isRightClicked) }
        lastMouseX = mouseX
        lastMouseY = mouseY
        previousLeftClickState = isLeftClicked
        previousRightClickState = isRightClicked
    }

    private fun updateElement(element: AbstractElement, mouseX: Double, mouseY: Double, isLeftClicked: Boolean, isRightClicked: Boolean) {
        element.apply {
            handleMouseHover(mouseX, mouseY)

            if (wasHovered) {
                HudEngine.clientApi.triggerEvent(
                    MouseHoverMoveEvent(MinecraftClient.getInstance(), mouseX, mouseY, lastMouseX, lastMouseY, this)
                )
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

    fun render(drawContext: DrawContext, tickDelta: Float) = elements.forEach { it.transformAndRender(drawContext, tickDelta) }
    fun elements(): MutableList<AbstractElement> = elements
}