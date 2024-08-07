package ru.airdead.hudrenderer.utility

open class V2(
    val x: Double = 0.0,
    val y: Double = 0.0
)

open class V3(
    x: Double = 0.0,
    y: Double = 0.0,
    val z: Double = 0.0
) : V2(x, y)


open class Rotation(
    val degrees: Float
)

infix fun Number.x(other: Number): V3 = V3(this.toDouble(), other.toDouble(), 0.0)