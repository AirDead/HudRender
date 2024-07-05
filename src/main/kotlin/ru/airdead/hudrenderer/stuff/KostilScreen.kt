package ru.airdead.hudrenderer.stuff

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import ru.airdead.hudrenderer.utility.Modifiers
import ru.airdead.hudrenderer.UIManager
import ru.airdead.hudrenderer.element.ContextMenu

/**
 * Custom screen class used to handle context menus.
 * Inherits from [Screen].
 */
class KostilScreen : Screen(Text.empty()) {

    /**
     * Handles actions on context menus.
     *
     * @param action The action to be performed on each context menu.
     */
    inline fun handleContextMenus(action: (ContextMenu) -> Unit) {
        UIManager.elements().asSequence()
            .filter { it is ContextMenu && it.enabled }
            .map { it as ContextMenu }
            .forEach(action)
    }

    /**
     * Determines whether the screen should close when the escape key is pressed.
     *
     * @return True if the screen should close, false otherwise.
     */
    override fun shouldCloseOnEsc(): Boolean {
        handleContextMenus { it.hide() }
        return super.shouldCloseOnEsc()
    }

    /**
     * Handles key press events.
     *
     * @param keyCode The key code of the pressed key.
     * @param scanCode The scan code of the pressed key.
     * @param modifiers The modifiers active during the key press.
     * @return True if the key press was handled, false otherwise.
     */
    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        val modifierSet = mutableSetOf<Modifiers>().apply {
            if (modifiers and 0x01 != 0) add(Modifiers.SHIFT)
            if (modifiers and 0x02 != 0) add(Modifiers.CONTROL)
            if (modifiers and 0x04 != 0) add(Modifiers.ALT)
            if (modifiers and 0x08 != 0) add(Modifiers.META)
        }

        handleContextMenus { it.handleKeyPressed(keyCode, modifierSet) }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    /**
     * Handles mouse scroll events.
     *
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param amount The amount of scroll.
     * @return True if the scroll event was handled, false otherwise.
     */
    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        handleContextMenus { it.handleScroll(mouseX, mouseY, amount) }
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    /**
     * Handles mouse drag events.
     *
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param button The mouse button being dragged.
     * @param deltaX The change in x-coordinate during the drag.
     * @param deltaY The change in y-coordinate during the drag.
     * @return True if the drag event was handled, false otherwise.
     */
    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        handleContextMenus { it.handleDrag(mouseX, mouseY, deltaX, deltaY) }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }
}
