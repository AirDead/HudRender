package ru.airdead.hudrenderer.utility

import ru.airdead.hudrenderer.element.ContextMenu
import ru.airdead.hudrenderer.element.line.DashedLineElement
import ru.airdead.hudrenderer.element.rectangle.RectangleElement
import ru.airdead.hudrenderer.element.text.ColorTextElement
import ru.airdead.hudrenderer.element.text.TextElement
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * DSL marker for element builders.
 */
@DslMarker
annotation class ElementBuilderDsl

/**
 * Inline function to create and configure a [RectangleElement].
 *
 * @param setup The configuration block for the [RectangleElement].
 * @return The configured [RectangleElement].
 */
@OptIn(ExperimentalContracts::class)
inline fun rectangle(setup: RectangleElement.() -> Unit): RectangleElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return RectangleElement().also(setup)
}

/**
 * Inline function to create and configure a [ContextMenu].
 *
 * @param setup The configuration block for the [ContextMenu].
 * @return The configured [ContextMenu].
 */
@OptIn(ExperimentalContracts::class)
inline fun menu(setup: ContextMenu.() -> Unit): ContextMenu {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return ContextMenu().also(setup)
}

/**
 * Inline function to create and configure a [TextElement].
 *
 * @param setup The configuration block for the [TextElement].
 * @return The configured [TextElement].
 */
@OptIn(ExperimentalContracts::class)
inline fun text(setup: TextElement.() -> Unit): TextElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return TextElement().also(setup)
}

/**
 * Inline function to create and configure a [ColorTextElement].
 *
 * @param setup The configuration block for the [ColorTextElement].
 * @return The configured [ColorTextElement].
 */
@OptIn(ExperimentalContracts::class)
inline fun colorText(setup: ColorTextElement.() -> Unit): ColorTextElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return ColorTextElement().also(setup)
}

@OptIn(ExperimentalContracts::class)
inline fun line(setup: DashedLineElement.() -> Unit): DashedLineElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return DashedLineElement().apply(setup)
}

