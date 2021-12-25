package io.github.muqhc.regionmaker.editer

import io.github.muqhc.regionmaker.plugin.RegionMakerPlugin
import io.github.muqhc.regionmaker.region.GroupRegion
import io.github.muqhc.regionmaker.render.GroupRegionRenderer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class GroupRegionEditor(
    val regionMakerPlugin: RegionMakerPlugin,
    override val player: Player,
    override val region: GroupRegion,
    override val renderer: GroupRegionRenderer = GroupRegionRenderer(player, region)
) : RegionEditor<GroupRegion,GroupRegionRenderer>(regionMakerPlugin) {

    override val commandRegex: Regex = Regex(
        """^(add|remove) region .+$"""
    )

    override val document: String = """
        |  Command :
        |    
        |    add region <region-name> := add the region, <region-name> to the region you're editing
        |    remove region <region-name> := remove the region, <region-name> in the region you're editing
        |    
    """.trimIndent()

    override fun onCommand(command: String) {
        val commandArgs = command.split(' ')
        when(commandArgs[0]){
            "add" -> region.regions.add(regionMakerPlugin.regionsManager.getRegion(commandArgs[2])!!)
            "remove" -> region.regions.remove(regionMakerPlugin.regionsManager.getRegion(commandArgs[2])!!)
        }
    }

}