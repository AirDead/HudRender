package ru.airdead.hudrenderer.utility

/**
 * Type alias for a handler function that processes click context.
 */
typealias ClickHandler = ClickContext.() -> Unit

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
 * Data class representing the context of a click event.
 *
 * @property pressed Indicates whether the button is pressed.
 * @property mouse The mouse object.
 */
data class ClickContext(val pressed: Boolean, val mouseX: Double, val mouseY: Double)