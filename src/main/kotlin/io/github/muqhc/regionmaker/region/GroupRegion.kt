package io.github.muqhc.regionmaker.region

import io.github.muqhc.regionmaker.editer.GroupRegionEditor
import io.github.muqhc.regionmaker.editer.RegionEditor
import io.github.muqhc.regionmaker.plugin.RegionMakerPlugin
import io.github.muqhc.regionmaker.render.GroupRegionRenderer
import io.github.muqhc.regionmaker.render.RegionRenderer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector


class GroupRegion(override val name: String) : Region {

    val regions: MutableList<Region> = mutableListOf()

    override var centerNode: Vector? = null
    override val nodes: MutableList<Vector>? = null

    override fun getDefaultSupportingEditor(plugin: Plugin, player: Player): RegionEditor<*, *> =
        GroupRegionEditor(plugin as RegionMakerPlugin, player, this)

    override fun getDefaultSupportingRenderer(player: Player): RegionRenderer<*> =
        GroupRegionRenderer(player, this)

    override fun isInside(x: Double, y: Double, z: Double): Boolean {
        regions.forEach {
            if (it.isInside(x,y,z)) return true
        }
        return false
    }
}