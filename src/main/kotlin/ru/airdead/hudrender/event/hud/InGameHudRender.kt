package ru.airdead.hudrender.event.hud

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrender.event.HudEvent

/**
 * Represents an event for rendering the in-game HUD.
 *
 * @property client The Minecraft client instance.
 * @property drawContext The context used for drawing the HUD.
 * @property tickDelta The delta time between ticks.
 */
data class InGameHudRender(
    override val client: MinecraftClient,
    val drawContext: DrawContext,
    val tickDelta: Float
) : HudEvent