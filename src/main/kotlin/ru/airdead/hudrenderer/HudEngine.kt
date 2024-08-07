package ru.airdead.hudrenderer

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import org.lwjgl.glfw.GLFW
import ru.airdead.hudrenderer.event.MouseMoveEvent
import ru.airdead.hudrenderer.stuff.ClientApi
import ru.airdead.hudrenderer.stuff.KostilScreen

/**
 * The `HudEngine` object is responsible for initializing and managing the HUD rendering and client tick events.
 */
object HudEngine {
    var clientApi: ClientApi = ClientApi()
    private var isInitialized = false
    var isHudHide = false
    private var previousMouseX = Double.NaN
    private var previousMouseY = Double.NaN

    /**
     * Initializes the HUD engine. Registers the HUD render callback and client tick event.
     * This method should only be called once.
     */
    @JvmStatic
    fun initialize() {
        if (isInitialized) return
        isInitialized = true

        HudRenderCallback.EVENT.register { drawContext, tickDelta ->
            HudManager.render(drawContext, tickDelta)
        }

        ClientTickEvents.START_CLIENT_TICK.register {
            clientApi.minecraft()?.let { client ->
                val (mouseX, mouseY) = client.mouse.run { x / 2 to y / 2 }
                val isLeftClicked = GLFW.glfwGetMouseButton(client.window.handle, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS
                val isRightClicked = GLFW.glfwGetMouseButton(client.window.handle, GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS
                HudManager.update(mouseX, mouseY, isLeftClicked, isRightClicked)

                if (client.currentScreen !is KostilScreen) return@register
                if (mouseX != previousMouseX || mouseY != previousMouseY) {
                    previousMouseX = mouseX
                    previousMouseY = mouseY / 2 // Minecraft incorrectly calculates the location of the mouse, so division is required here
                    clientApi.triggerEvent(MouseMoveEvent(client, mouseX, mouseY / 2, previousMouseX, previousMouseY))
                }
            }
        }
    }

    /**
     * Uninitializes the HUD engine. Resets the initialization flag.
     */
    @JvmStatic
    fun unInitialize() {
        isInitialized = false
    }
}