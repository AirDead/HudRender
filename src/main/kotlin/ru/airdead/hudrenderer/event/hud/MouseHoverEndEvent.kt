package ru.airdead.hudrenderer.event.hud

import net.minecraft.client.MinecraftClient
import ru.airdead.hudrenderer.element.AbstractElement
import ru.airdead.hudrenderer.event.HudEvent

/**
 * Represents an event that occurs when the mouse hover ends on an element.
 *
 * @property client The Minecraft client instance.
 * @property mouseX The x-coordinate of the mouse cursor.
 * @property mouseY The y-coordinate of the mouse cursor.
 * @property element The element that the mouse was hovering over.
 */
data class MouseHoverEndEvent(
    override val client: MinecraftClient,
    val mouseX: Double,
    val mouseY: Double,
    val element: AbstractElement
) : HudEvent