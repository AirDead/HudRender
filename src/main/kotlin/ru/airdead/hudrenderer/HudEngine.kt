package ru.airdead.hudrenderer

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import org.lwjgl.glfw.GLFW
import ru.airdead.hudrenderer.event.EventManager.triggerEvent
import ru.airdead.hudrenderer.event.hud.MouseMoveEvent
import ru.airdead.hudrenderer.stuff.ClientApi
import ru.airdead.hudrenderer.stuff.KostilScreen

/**
 * The `HudEngine` object is responsible for initializing and managing the HUD rendering and client tick events.
 */
object HudEngine {
    val clientApi: ClientApi = ClientApi()
    var isHudHide = false
    private var isInitialized = false
    private var previousMouseX = Double.NaN
    private var previousMouseY = Double.NaN

    /**
     * Initializes the HUD engine. Registers the HUD render callback and client tick event.
     * This method should only be called once.
     */
    @JvmStatic
    fun initialize() {
        ClientTickEvents.START_CLIENT_TICK.register(::onClientTick)
    }

    /**
     * Handles the client tick event. Initializes the HUD engine if it hasn't been initialized yet.
     * @param client The Minecraft client instance.
     */
    private fun onClientTick(client: MinecraftClient) {
        if (!isInitialized) {
            MinecraftClient.getInstance()?.let {
                registerCallbacks()
                isInitialized = true
            }
        } else {
            handleClientTick(client)
        }
    }

    /**
     * Registers the HUD render callback.
     * The callback is responsible for rendering the HUD.
     */
    private fun registerCallbacks() {
        HudRenderCallback.EVENT.register { drawContext, tickDelta ->
            HudManager.render(drawContext, tickDelta)
        }
    }

    /**
     * Handles the client tick event. Updates the mouse position and triggers the mouse move event.
     * @param client The Minecraft client instance.
     * @see MouseMoveEvent
     */
    private fun handleClientTick(client: MinecraftClient) {
        val (mouseX, mouseY) = getMousePosition(client)
        val isLeftClicked = isMouseClicked(client, GLFW.GLFW_MOUSE_BUTTON_1)
        val isRightClicked = isMouseClicked(client, GLFW.GLFW_MOUSE_BUTTON_2)
        HudManager.update(mouseX, mouseY, isLeftClicked, isRightClicked)

        if (client.currentScreen is KostilScreen) {
            handleMouseMoveEvent(client, mouseX, mouseY)
        }
    }

    /**
     * Gets the current mouse position.
     * @param client The Minecraft client instance.
     * @return The current mouse position.
     */
    fun getMousePosition(client: MinecraftClient) = client.mouse.run { x / 2 to y / 2 }

    /**
     * Checks if the mouse button is clicked.
     * @param client The Minecraft client instance.
     * @param button The mouse button to check.
     * @return `true` if the mouse button is clicked, `false` otherwise.
     */
    fun isMouseClicked(client: MinecraftClient, button: Int) =
        GLFW.glfwGetMouseButton(client.window.handle, button) == GLFW.GLFW_PRESS

    /**
     * Handles the mouse move event. Triggers the mouse move event if the mouse position has changed.
     * @param client The Minecraft client instance.
     * @param mouseX The current mouse X position.
     * @param mouseY The current mouse Y position.
     */
    private fun handleMouseMoveEvent(client: MinecraftClient, mouseX: Double, mouseY: Double) {
        val adjustedMouseY = mouseY / 2 // Minecraft incorrectly calculates the location of the mouse, so division is required here
        if (mouseX != previousMouseX || adjustedMouseY != previousMouseY) {
            triggerEvent(MouseMoveEvent(client, mouseX, adjustedMouseY, previousMouseX, previousMouseY))
            previousMouseX = mouseX
            previousMouseY = adjustedMouseY
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