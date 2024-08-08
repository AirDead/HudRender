package ru.airdead.hudrenderer.event.hud

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import ru.airdead.hudrenderer.event.HudEvent

data class InGameHudRender(
    override val client: MinecraftClient,
    val drawContext: DrawContext,
    val tickDelta: Float
) : HudEvent