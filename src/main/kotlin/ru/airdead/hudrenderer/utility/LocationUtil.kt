package ru.airdead.hudrenderer.utility

import ru.airdead.hudrenderer.element.AbstractElement

fun getLocationByOrigin(element: AbstractElement, origin: V3): V3 {
    val parentSize = element.lastParent?.size ?: V3(0.0, 0.0, 0.0)
    return V3(
        calculateAbsolutePosition(parentSize.x, element.size.x, origin.x, origin.x, element.lastParent?.renderLocation?.x, 0.0),
        calculateAbsolutePosition(parentSize.y, element.size.y, origin.y, origin.y, element.lastParent?.renderLocation?.y, 0.0),
        element.renderLocation.z
    )
}

fun getLocationByAlign(element: AbstractElement, align: V3): V3 {
    val parentSize = element.lastParent?.size ?: V3(0.0, 0.0, 0.0)
    return V3(
        calculateAbsolutePosition(parentSize.x, element.size.x, align.x, align.x, element.lastParent?.renderLocation?.x, 0.0),
        calculateAbsolutePosition(parentSize.y, element.size.y, align.y, align.y, element.lastParent?.renderLocation?.y, 0.0),
        element.renderLocation.z
    )
}

fun getLocationByOriginAndOffset(element: AbstractElement, origin: V3, offset: V3): V3 {
    val parentSize = element.lastParent?.size ?: V3(0.0, 0.0, 0.0)
    return V3(
        calculateAbsolutePosition(parentSize.x, element.size.x, origin.x, origin.x, element.lastParent?.renderLocation?.x, offset.x),
        calculateAbsolutePosition(parentSize.y, element.size.y, origin.y, origin.y, element.lastParent?.renderLocation?.y, offset.y),
        element.renderLocation.z
    )
}

fun getLocationByAlignAndOffset(element: AbstractElement, align: V3, offset: V3): V3 {
    val parentSize = element.lastParent?.size ?: V3(0.0, 0.0, 0.0)
    return V3(
        calculateAbsolutePosition(parentSize.x, element.size.x, align.x, align.x, element.lastParent?.renderLocation?.x, offset.x),
        calculateAbsolutePosition(parentSize.y, element.size.y, align.y, align.y, element.lastParent?.renderLocation?.y, offset.y),
        element.renderLocation.z
    )
}

fun getLocationByOriginAlignAndOffset(element: AbstractElement, origin: V3, align: V3, offset: V3): V3 {
    val parentSize = element.lastParent?.size ?: V3(0.0, 0.0, 0.0)
    return V3(
        calculateAbsolutePosition(parentSize.x, element.size.x, align.x, origin.x, element.lastParent?.renderLocation?.x, offset.x),
        calculateAbsolutePosition(parentSize.y, element.size.y, align.y, origin.y, element.lastParent?.renderLocation?.y, offset.y),
        element.renderLocation.z
    )
}

private fun calculateAbsolutePosition(
    parentSize: Double, size: Double, align: Double, origin: Double, parentOffset: Double?, offset: Double
): Double {
    return parentSize * align - size * origin + (parentOffset ?: 0.0) + offset
}