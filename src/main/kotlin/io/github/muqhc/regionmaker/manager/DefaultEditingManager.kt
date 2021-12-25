package io.github.muqhc.regionmaker.manager

import io.github.muqhc.regionmaker.editer.RegionEditor
import io.github.muqhc.regionmaker.region.Region
import io.github.muqhc.regionmaker.render.RegionRenderer
import org.bukkit.entity.Player
import java.util.*

class DefaultEditingManager : EditingManager {
    override val editingPlayersMap: MutableMap<UUID, RegionEditor<*, *>> = mutableMapOf()

}