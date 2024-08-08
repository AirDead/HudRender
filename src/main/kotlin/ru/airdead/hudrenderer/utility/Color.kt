package ru.airdead.hudrenderer.utility

@JvmField val WHITE = Color(255, 255, 255, 1.0)
@JvmField val BLACK = Color(0, 0, 0, 1.0)
@JvmField val TRANSPARENT = Color(0, 0, 0, 0.0)

open class Color(
    open var red: Int = 0,
    open var green: Int = 0,
    open var blue: Int = 0,
    open var alpha: Double = 1.0
) {
    fun write(another: Color) {
        another.red = this.red
        another.green = this.green
        another.blue = this.blue
        another.alpha = this.alpha
    }

    fun toInt(): Int {
        val alphaInt = (alpha * 255).toInt().coerceIn(0, 255)
        return java.awt.Color(red, green, blue, alphaInt).rgb
    }

    fun copy(): Color {
        return Color(this.red, this.green, this.blue, this.alpha)
    }

    fun interpolate(other: Color, progress: Double) = Color(
        (red + (other.red - red) * progress).toInt(),
        (green + (other.green - green) * progress).toInt(),
        (blue + (other.blue - blue) * progress).toInt(),
        alpha + (other.alpha - alpha) * progress
    )
}
