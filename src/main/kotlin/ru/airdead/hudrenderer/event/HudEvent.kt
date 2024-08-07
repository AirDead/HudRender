package ru.airdead.hudrenderer.event

import net.minecraft.client.MinecraftClient
import ru.airdead.hudrenderer.element.AbstractElement

interface HudEvent {
    val client: MinecraftClient
}

data class MouseMoveEvent(
    override val client: MinecraftClient,
    val mouseX: Double,
    val mouseY: Double,
    val lastX: Double,
    val lastY: Double
) : HudEvent

data class MouseHoverMoveEvent(
    override val client: MinecraftClient,
    val mouseX: Double,
    val mouseY: Double,
    val lastX: Double,
    val lastY: Double,
    val element: AbstractElement
) : HudEvent