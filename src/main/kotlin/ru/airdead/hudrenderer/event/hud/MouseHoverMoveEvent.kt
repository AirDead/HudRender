package ru.airdead.hudrenderer.event.hud

import net.minecraft.client.MinecraftClient
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.event.HudEvent

/**
 * Represents an event that occurs when the mouse moves while hovering over an element.
 *
 * @property client The Minecraft client instance.
 * @property mouseX The current x-coordinate of the mouse cursor.
 * @property mouseY The current y-coordinate of the mouse cursor.
 * @property lastX The previous x-coordinate of the mouse cursor.
 * @property lastY The previous y-coordinate of the mouse cursor.
 * @property element The element that the mouse is hovering over.
 */
data class MouseHoverMoveEvent(
    override val client: MinecraftClient,
    val mouseX: Double,
    val mouseY: Double,
    val lastX: Double,
    val lastY: Double,
    val element: AbstractElement
) : HudEvent