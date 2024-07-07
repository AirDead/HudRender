package ru.airdead.hudrenderer

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import ru.airdead.hudrenderer.stuff.ClientApi

/**
 * Singleton object representing the UI engine.
 */
object HudEngine {

    /**
     * Instance of [ClientApi] used by the UI engine.
     */
    var clientApi: ClientApi = ClientApi()

    /**
     * Indicates whether the UI engine has been initialized.
     */
    private var isInitialized = false

    /**
     * Indicates whether the HUD is hidden.
     */
    var isHudHide = false

    /**
     * Initializes the UI engine.
     */
    @JvmStatic
    fun initialize() {
        if (isInitialized) return
        isInitialized = true

        HudRenderCallback.EVENT.register { drawContext, tickDelta ->
            HudManager.render(drawContext, tickDelta)
        }

        ClientTickEvents.START_CLIENT_TICK.register {
            val minecraft = clientApi.minecraft()
            val mouse = minecraft.mouse
            val (mouseX, mouseY) = mouse.x / 2 to mouse.y / 2
            val isLeftClicked = mouse.wasLeftButtonClicked()
            val isRightClicked = mouse.wasRightButtonClicked()
            HudManager.update(mouseX, mouseY, isLeftClicked, isRightClicked)
        }
    }

    /**
     * Uninitializes the UI engine.
     */
    @JvmStatic
    fun unInitialize() {
        isInitialized = false
    }
}