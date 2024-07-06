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