package ru.airdead.hudrender.utility

import ru.airdead.hudrender.element.AbstractElement

/**
 * Enumeration of mouse buttons.
 */
@Suppress("unused")
enum class MouseButton {
    LEFT,
    RIGHT,
    MIDDLE
}

/**
 * Enumeration of modifier keys.
 */
enum class Modifiers {
    SHIFT,
    CONTROL,
    ALT,
    META
}

/**
 * Type alias for a handler function that processes button context.
 */
typealias ButtonHandler = ButtonContext.() -> Unit

/**
 * Data class representing the context of a button event.
 *
 * @property key The key code of the button.
 * @property modifiers A set of modifiers applied during the button event.
 */
data class ButtonContext(val key: Int, val modifiers: Set<Modifiers>)

/**
 * Type alias for a handler function that processes click context.
 */
typealias ClickHandler = ClickContext.() -> Unit

/**
 * Data class representing the context of a click event.
 *
 * @property pressed Indicates whether the button is pressed.
 * @property mouseX The x coordinate of the mouse.
 * @property mouseY The y coordinate of the mouse.
 */
data class ClickContext(val isPressed: Boolean, val mouseX: Double, val mouseY: Double)

/**
 * Type alias for a handler function that processes hover context.
 */
typealias HoverHandler = HoverContext.() -> Unit

/**
 * Data class representing the context of a hover event.
 *
 * @property isHovered Indicates whether the element is hovered.
 * @property mouseX The x coordinate of the mouse.
 * @property mouseY The y coordinate of the mouse.
 */
data class HoverContext(val isHovered: Boolean, val mouseX: Double, val mouseY: Double)

/**
 * Type alias for a handler function that processes drag context.
 */
typealias DragHandler = (DragContext) -> Unit

/**
 * Data class representing the context of a drag event.
 *
 * @property element The element being dragged.
 * @property mouseX The x-coordinate of the mouse.
 * @property mouseY The y-coordinate of the mouse.
 * @property dx The change in x-coordinate during the drag.
 * @property dy The change in y-coordinate during the drag.
 */
data class DragContext(
    val element: AbstractElement,
    val mouseX: Double,
    val mouseY: Double,
    val dx: Double,
    val dy: Double
)

/**
 * Type alias for a handler function that processes scroll context.
 */
typealias ScrollHandler = ScrollContext.() -> Unit

/**
 * Data class representing the context of a scroll event.
 *
 * @property hoveredElement The element being hovered over during the scroll.
 * @property amount The amount of scroll.
 */
data class ScrollContext(val hoveredElement: AbstractElement?, val amount: Double)