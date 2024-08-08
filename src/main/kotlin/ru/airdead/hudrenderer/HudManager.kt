package ru.airdead.hudrenderer

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.element.Parent
import ru.airdead.hudrenderer.event.EventManager.triggerEvent
import ru.airdead.hudrenderer.event.hud.MouseHoverEndEvent
import ru.airdead.hudrenderer.event.hud.MouseHoverMoveEvent
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
        updatePreviousStates(mouseX, mouseY, isLeftClicked, isRightClicked)
    }

    private fun updatePreviousStates(mouseX: Double, mouseY: Double, isLeftClicked: Boolean, isRightClicked: Boolean) {
        lastMouseX = mouseX
        lastMouseY = mouseY / 2
        previousLeftClickState = isLeftClicked
        previousRightClickState = isRightClicked
    }

    private fun updateElement(element: AbstractElement, mouseX: Double, mouseY: Double, isLeftClicked: Boolean, isRightClicked: Boolean) {
        val wasHoveredBefore = element.wasHovered
        element.apply {
            handleMouseHover(mouseX, mouseY)
            triggerMouseHoverEvents(wasHoveredBefore, mouseX, mouseY)
            handleClickEvents(isLeftClicked, previousLeftClickState, MouseButton.LEFT, mouseX, mouseY)
            handleClickEvents(isRightClicked, previousRightClickState, MouseButton.RIGHT, mouseX, mouseY)

            if (this is Parent) children.forEach { updateElement(it, mouseX, mouseY, isLeftClicked, isRightClicked) }
        }
    }

    private fun AbstractElement.triggerMouseHoverEvents(wasHoveredBefore: Boolean, mouseX: Double, mouseY: Double) {
        when {
            wasHovered && wasHoveredBefore -> triggerEvent(MouseHoverMoveEvent(MinecraftClient.getInstance(), mouseX, mouseY / 2, lastMouseX, lastMouseY, this))
            !wasHovered && wasHoveredBefore -> triggerEvent(MouseHoverEndEvent(MinecraftClient.getInstance(), mouseX, mouseY / 2, this))
        }
    }

    private fun AbstractElement.handleClickEvents(isClicked: Boolean, previousClickState: Boolean, button: MouseButton, mouseX: Double, mouseY: Double) {
        if (isClicked != previousClickState) {
            handleMouseClick(button, ClickContext(isClicked, mouseX, mouseY / 2))
        }
    }

    fun render(drawContext: DrawContext, tickDelta: Float) = elements.forEach { it.transformAndRender(drawContext, tickDelta) }
    fun elements(): MutableList<AbstractElement> = elements
}