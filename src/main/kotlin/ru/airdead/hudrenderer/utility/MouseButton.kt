package ru.airdead.hudrenderer.utility

enum class MouseButton {
    LEFT,
    RIGHT,
    MIDDLE
}

typealias ButtonHandler = ButtonContext.() -> Unit

data class ButtonContext(val key: Int, val modifiers: Set<Modifiers>)