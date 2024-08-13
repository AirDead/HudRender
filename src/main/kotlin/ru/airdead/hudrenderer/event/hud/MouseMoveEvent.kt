package ru.airdead.hudrenderer.event.hud

import net.minecraft.client.MinecraftClient
import ru.airdead.hudrenderer.event.HudEvent

/**
 * Represents an event that occurs when the mouse moves.
 *
 * @property client The Minecraft client instance.
 * @property mouseX The current x-coordinate of the mouse cursor.
 * @property mouseY The current y-coordinate of the mouse cursor.
 * @property lastX The previous x-coordinate of the mouse cursor.
 * @property lastY The previous y-coordinate of the mouse cursor.
 */
data class MouseMoveEvent(
    override val client: MinecraftClient,
    val mouseX: Double,
    val mouseY: Double,
    val lastX: Double,
    val lastY: Double
) : HudEvent