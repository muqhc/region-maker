package io.github.muqhc.regionmaker.region

import io.github.muqhc.regionmaker.editer.RegionEditor
import io.github.muqhc.regionmaker.render.RegionRenderer
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import java.io.Serializable

interface Region : Serializable {

    val name: String

    var centerNode: Vector?
    val nodes: MutableList<Vector>?

    fun getDefaultSupportingEditor(plugin: Plugin, player: Player): RegionEditor<*, *>

    fun getDefaultSupportingRenderer(player: Player): RegionRenderer<*>

    fun isInside(x: Double, y: Double, z: Double): Boolean
    infix fun isInside(xyz: Triple<Double,Double,Double>): Boolean = xyz.run{ isInside(first, second, third) }
    infix fun isInside(vector3D: Vector): Boolean = vector3D.run{ isInside(x,y,z) }
    infix fun isInside(location: Location): Boolean = location.run{ isInside(x,y,z) }
    infix fun isInside(block: Block): Boolean = isInside(block.location)
    infix fun isInside(entity: Entity): Boolean = isInside(entity.location)
}