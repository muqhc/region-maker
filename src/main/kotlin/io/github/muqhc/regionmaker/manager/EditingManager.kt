package io.github.muqhc.regionmaker.manager

import io.github.muqhc.regionmaker.editer.RegionEditor
import org.bukkit.entity.Player
import java.util.*

interface EditingManager {
    val editingPlayersMap: MutableMap<UUID, RegionEditor<*, *>>

    fun addEditor(player: Player, editor: RegionEditor<*, *>) {
        editor.initialize()
        editingPlayersMap[player.uniqueId] = editor
    }

    fun getEditorsAsImmutable() = editingPlayersMap.toMap()
    fun getUUIDs() = editingPlayersMap.keys.toSet()
    fun getEditor(uuid: UUID) = editingPlayersMap[uuid]

    fun removeEditor(uuid: UUID) {
        editingPlayersMap[uuid]?.disable()
        editingPlayersMap.remove(uuid)
    }
}