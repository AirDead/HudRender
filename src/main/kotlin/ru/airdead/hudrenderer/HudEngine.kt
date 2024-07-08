package ru.airdead.hudrenderer

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import ru.airdead.hudrenderer.stuff.ClientApi

object HudEngine {

    var clientApi: ClientApi = ClientApi()
    private var isInitialized = false
    var isHudHide = false

    @JvmStatic
    fun initialize() {
        if (isInitialized) return
        isInitialized = true

        HudRenderCallback.EVENT.register { drawContext, tickDelta ->
            HudManager.render(drawContext, tickDelta)
        }

        ClientTickEvents.START_CLIENT_TICK.register {
            val minecraft = clientApi.minecraft()
            minecraft?.let {
                val mouse = it.mouse
                val (mouseX, mouseY) = mouse.x / 2 to mouse.y / 2
                val isLeftClicked = mouse.wasLeftButtonClicked()
                val isRightClicked = mouse.wasRightButtonClicked()
                HudManager.update(mouseX, mouseY, isLeftClicked, isRightClicked)
            }
        }
    }

    @JvmStatic
    fun unInitialize() {
        isInitialized = false
    }
}