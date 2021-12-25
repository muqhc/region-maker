package io.github.muqhc.regionmaker.plugin

import io.github.muqhc.regionmaker.command.KommandRegionMaker
import io.github.muqhc.regionmaker.manager.DefaultEditingManager
import io.github.muqhc.regionmaker.manager.DefaultRegionsManager
import io.github.muqhc.regionmaker.manager.EditingManager
import io.github.muqhc.regionmaker.manager.RegionsManager
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Muqhc
 */
class RegionMakerPlugin : JavaPlugin() {
    lateinit var regionsManager: RegionsManager
    lateinit var editingManager: EditingManager
    override fun onEnable() {
        regionsManager = DefaultRegionsManager()
        editingManager = DefaultEditingManager()
        KommandRegionMaker.register(this, regionsManager, editingManager)
    }
}