package io.github.muqhc.regionmaker.plugin

import io.github.muqhc.regionmaker.command.KommandRegionMaker
import io.github.muqhc.regionmaker.listener.EventListener
import io.github.muqhc.regionmaker.manager.DefaultEditingManager
import io.github.muqhc.regionmaker.manager.DefaultRegionsManager
import io.github.muqhc.regionmaker.manager.EditingManager
import io.github.muqhc.regionmaker.manager.RegionsManager
import io.github.muqhc.regionmaker.region.Region
import io.github.muqhc.runction.bind
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * @author Muqhc
 */
class RegionMakerPlugin : JavaPlugin() {

    lateinit var regionsManager: RegionsManager
    lateinit var editingManager: EditingManager

    lateinit var savingName: String

    val regionsManagerContainerFolder = File(dataFolder,"regionSave")

    fun Player.getUsingEditor() =
        editingManager.getEditor(uniqueId)

    override fun onEnable() {
        dataFolder.mkdirs()
        if (File(dataFolder, "config.yml").createNewFile())
            saveDefaultConfig()

        savingName = config.getString("savingName") ?: run {
            config.set("savingName", "default")
            "default"
        }

        regionsManager = loadRegionManager() ?: DefaultRegionsManager()
        editingManager = DefaultEditingManager()

        server.pluginManager.registerEvents(EventListener(this), this)
        KommandRegionMaker.register(this, regionsManager, editingManager)
    }

    override fun onDisable() {
        saveRegionManager()
        saveConfig()
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun loadRegionManager(name: String = savingName): RegionsManager? {
        regionsManagerContainerFolder.mkdirs()

        val regionsManagerFiles = regionsManagerContainerFolder.listFiles()

        regionsManagerFiles.forEach {
            if (it.name == "$name.dat") return it.readText() bind DefaultRegionsManager()::deserialize
        }

        return null
    }

    fun saveRegionManager(name: String = savingName) {
        regionsManagerContainerFolder.mkdirs()

        File(regionsManagerContainerFolder, "$name.dat").apply {
            writeText(regionsManager.serialize())
            createNewFile()
        }
    }
}