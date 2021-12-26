package io.github.muqhc.regionmaker.listener

import io.github.muqhc.regionmaker.plugin.RegionMakerPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class EventListener(val plugin: RegionMakerPlugin) : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        plugin.run {
            event.player.getUsingEditor()?.onInteract(event)
        }
    }
}