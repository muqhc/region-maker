package io.github.muqhc.regionmaker.region

import io.github.muqhc.regionmaker.editer.PrismRegionEditor
import io.github.muqhc.regionmaker.editer.RegionEditor
import io.github.muqhc.regionmaker.render.PrismRegionRenderer
import io.github.muqhc.regionmaker.render.RegionRenderer
import io.github.muqhc.regionmaker.util.Polygon
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector

class PrismRegion(override val name: String): Region, Polygon() {

    var bottomY: Int? = null
    var topY: Int? = null

    override var centerNode: Vector? = null
    override val nodes: MutableList<Vector>? = null

    override fun getDefaultSupportingEditor(plugin: Plugin, player: Player): RegionEditor<*, *> =
        PrismRegionEditor(plugin, player, this)

    override fun getDefaultSupportingRenderer(player: Player): RegionRenderer<*> =
        PrismRegionRenderer(player,this)

    override fun isInside(x: Double, y: Double, z: Double): Boolean {

        bottomY?.let { if (it>y) return false }
        topY?.let { if (it<y) return false }

        var crossCount = 0
        val futurePoints = points.drop(1) + points.first()

        (points zip futurePoints).forEach {
            it.run {
                if ((first.y > z) xor (second.y > z)) {
                    val onTheLineX =
                        (first.x-second.x)/(first.y-second.y) * (z-first.y) + first.x
                    if (onTheLineX > x) crossCount += 1
                }
            }
        }
        return crossCount % 2 == 1
    }

}
