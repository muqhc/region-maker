package io.github.muqhc.regionmaker.render

import io.github.muqhc.regionmaker.region.GroupRegion
import org.bukkit.entity.Player

class GroupRegionRenderer(override val player: Player, override val region: GroupRegion) : RegionRenderer<GroupRegion>() {
    lateinit var innerRenderers : List<RegionRenderer<*>>

    override fun onInitialize() {
        innerRenderers = region.regions.map { it.getDefaultSupportingRenderer(player) }
        innerRenderers.forEach {
            it.initialize()
        }
    }

    fun update() {
        innerRenderers = region.regions.map { it.getDefaultSupportingRenderer(player) }
        innerRenderers.forEach {
            it.initialize()
        }
    }

    override fun onRender() {
        innerRenderers.forEach {
            it.render()
        }
    }

    override fun onDisable() {
        innerRenderers.forEach {
            it.disable()
        }
    }
}