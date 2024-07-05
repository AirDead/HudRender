package ru.airdead.hudrenderer.utility

/**
 * Predefined color constants.
 */
@JvmField val WHITE = Color(255, 255, 255, 1.0)
@JvmField val BLACK = Color(0, 0, 0, 1.0)
@JvmField val TRANSPARENT = Color(0, 0, 0, 0.0)

/**
 * Class representing a color with red, green, blue, and alpha (transparency) components.
 *
 * @property red The red component of the color (0-255).
 * @property green The green component of the color (0-255).
 * @property blue The blue component of the color (0-255).
 * @property alpha The alpha component of the color (0.0-1.0).
 */
open class Color(
    open var red: Int = 0,
    open var green: Int = 0,
    open var blue: Int = 0,
    open var alpha: Double = 1.0
) {

    /**
     * Writes the values of this color to another color instance.
     *
     * @param another The color instance to which the values will be written.
     */
    open fun write(another: Color) {
        another.red = this.red
        another.green = this.green
        another.blue = this.blue
        another.alpha = this.alpha
    }

    /**
     * Converts the color to an integer representation.
     *
     * @return The integer representation of the color.
     */
    fun toInt(): Int {
        val alphaInt = (alpha * 255).toInt().coerceIn(0, 255)
        return java.awt.Color(red, green, blue, alphaInt).rgb
    }

    /**
     * Creates a copy of the color instance.
     *
     * @return A new [Color] instance with the same values.
     */
    fun copy(): Color {
        return Color(this.red, this.green, this.blue, this.alpha)
    }
}
