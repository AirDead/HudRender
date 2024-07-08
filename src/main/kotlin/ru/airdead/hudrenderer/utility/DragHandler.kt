package ru.airdead.hudrenderer.utility

import ru.airdead.hudrenderer.element.AbstractElement

typealias DragHandler = (DragContext) -> Unit

/**
 * Data class representing the context of a drag event.
 *
 * @param element The element being dragged.
 * @param mouseX The x-coordinate of the mouse.
 * @param mouseY The y-coordinate of the mouse.
 * @param dx The change in x-coordinate during the drag.
 * @param dy The change in y-coordinate during the drag.
 */
data class DragContext(
    val element: AbstractElement,
    val mouseX: Double,
    val mouseY: Double,
    val dx: Double,
    val dy: Double
)