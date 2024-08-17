package ru.airdead.hudrender.mixins

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.hud.InGameHud
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import ru.airdead.hudrender.HudEngine
import ru.airdead.hudrender.event.EventManager.triggerEvent
import ru.airdead.hudrender.event.hud.InGameHudRender

/**
 * Mixin class for modifying the in-game HUD rendering.
 */
@Suppress("unused", "SpellCheckingInspection")
@Mixin(InGameHud::class)
class InGameHudMixin {

    /**
     * Injects into the renderHotbar method to conditionally cancel its execution.
     *
     * @param tickDelta The delta time since the last tick.
     * @param context The draw context used for rendering.
     * @param ci The callback info used to cancel the method execution.
     */
    @Inject(method = ["renderHotbar"], at = [At("HEAD")], cancellable = true)
    fun renderHotbar(tickDelta: Float, context: DrawContext?, ci: CallbackInfo) {
        if (HudEngine.isHudHide) {
            ci.cancel()
        }

        val client = HudEngine.clientApi.minecraft() ?: return
        triggerEvent(
            InGameHudRender(
                client,
                context!!,
                tickDelta
            )
        )
    }
}
