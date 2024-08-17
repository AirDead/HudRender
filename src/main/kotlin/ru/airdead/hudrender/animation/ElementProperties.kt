package ru.airdead.hudrender.animation

import ru.airdead.hudrender.element.AbstractElement
import ru.airdead.hudrender.utility.Color
import ru.airdead.hudrender.utility.Rotation
import ru.airdead.hudrender.utility.V3

/**
 * Data class representing the properties of an element.
 *
 * @property size The size of the element.
 * @property color The color of the element.
 * @property strokeColor The stroke color of the element.
 * @property align The alignment of the element.
 * @property origin The origin point of the element.
 * @property offset The offset of the element.
 * @property rotation The rotation of the element.
 */
data class ElementProperties(
    var size: V3,
    var color: Color,
    var strokeColor: Color,
    var align: V3,
    var origin: V3,
    var offset: V3,
    var rotation: Rotation
) {
    /**
     * Interpolates between this ElementProperties and another ElementProperties.
     *
     * @param other The other ElementProperties to interpolate with.
     * @param progress The interpolation progress (0.0 to 1.0).
     * @return A new ElementProperties instance with interpolated values.
     */
    fun interpolate(other: ElementProperties, progress: Double) = ElementProperties(
        size.interpolate(other.size, progress),
        color.interpolate(other.color, progress),
        strokeColor.interpolate(other.strokeColor, progress),
        align.interpolate(other.align, progress),
        origin.interpolate(other.origin, progress),
        offset.interpolate(other.offset, progress),
        rotation.interpolate(other.rotation, progress)
    )
}

/**
 * Extension function to create a copy of the properties of an AbstractElement.
 *
 * @receiver The AbstractElement to copy properties from.
 * @return A new ElementProperties instance with copied values.
 */
fun AbstractElement.copyProperties() = ElementProperties(
    size.copy(),
    color.copy(),
    strokeColor.copy(),
    align.copy(),
    origin.copy(),
    offset.copy(),
    rotation.copy()
)

/**
 * Extension function to apply properties to an AbstractElement.
 *
 * @receiver The AbstractElement to apply properties to.
 * @param properties The ElementProperties to apply.
 */
fun AbstractElement.applyProperties(properties: ElementProperties) {
    size = properties.size
    color = properties.color
    strokeColor = properties.strokeColor
    align = properties.align
    origin = properties.origin
    offset = properties.offset
    rotation = properties.rotation
}
