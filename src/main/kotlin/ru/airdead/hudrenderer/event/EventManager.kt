package ru.airdead.hudrenderer.event

object EventManager {
    val eventListeners = mutableMapOf<Class<out Event>, MutableList<(Event) -> Unit>>()

    fun <T : Event> triggerEvent(event: T) {
        eventListeners[event::class.java]?.forEach { listener ->
            listener(event)
        }
    }
}