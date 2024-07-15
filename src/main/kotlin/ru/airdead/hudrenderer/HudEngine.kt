package ru.airdead.hudrenderer

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import org.lwjgl.glfw.GLFW
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
            clientApi.minecraft()?.let {
                val (mouseX, mouseY) = it.mouse.run { x / 2 to y / 2 }
                val isLeftClicked = GLFW.glfwGetMouseButton(it.window.handle, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS
                val isRightClicked = GLFW.glfwGetMouseButton(it.window.handle, GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS
                HudManager.update(mouseX, mouseY, isLeftClicked, isRightClicked)
            }
        }
    }

    @JvmStatic
    fun unInitialize() {
        isInitialized = false
    }
}
