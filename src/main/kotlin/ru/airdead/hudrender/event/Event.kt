package ru.airdead.hudrender.event

import net.minecraft.client.MinecraftClient

interface Event {
    val client: MinecraftClient
}

interface HudEvent: Event

interface PlayerEvent: Event

interface WorldEvent: Event