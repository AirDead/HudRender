package ru.airdead.hudrenderer.event.hud

import net.minecraft.client.MinecraftClient
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.event.HudEvent

data class MouseHoverMoveEvent(
    override val client: MinecraftClient,
    val mouseX: Double,
    val mouseY: Double,
    val lastX: Double,
    val lastY: Double,
    val element: AbstractElement
) : HudEvent
