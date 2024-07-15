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
 */
data class HoverContext(val isHovered: Boolean)

/**
 * Data class representing the context of a click event.
 *
 * @property pressed Indicates whether the button is pressed.
 */
data class ClickContext(val pressed: Boolean)