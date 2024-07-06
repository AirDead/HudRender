package ru.airdead.hudrenderer.utility

import ru.airdead.hudrenderer.element.AbstractElement

typealias ScrollHandler = ScrollContext.() -> Unit

data class ScrollContext(val hoveredElement: AbstractElement?, val amount: Double)