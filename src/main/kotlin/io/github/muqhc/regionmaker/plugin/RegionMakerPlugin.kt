package io.github.muqhc.regionmaker.plugin

import io.github.muqhc.regionmaker.command.KommandRegionMaker
import io.github.muqhc.regionmaker.listener.EventListener
import io.github.muqhc.regionmaker.manager.DefaultEditingManager
import io.github.muqhc.regionmaker.manager.DefaultRegionsManager
import io.github.muqhc.regionmaker.manager.EditingManager
import io.github.muqhc.regionmaker.manager.RegionsManager
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Muqhc
 */
class RegionMakerPlugin : JavaPlugin() {

    lateinit var regionsManager: RegionsManager
    lateinit var editingManager: EditingManager

    fun Player.getUsingEditor() =
        editingManager.getEditor(uniqueId)

    override fun onEnable() {
        regionsManager = DefaultRegionsManager()
        editingManager = DefaultEditingManager()
        server.pluginManager.registerEvents(EventListener(this), this)
        KommandRegionMaker.register(this, regionsManager, editingManager)
    }
}