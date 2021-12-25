package io.github.muqhc.regionmaker.render

import io.github.muqhc.regionmaker.region.PrismRegion
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import java.lang.Double.min
import kotlin.math.sign

class PrismRegionRenderer(override val player: Player, override val region: PrismRegion) : RegionRenderer<PrismRegion>() {
    var times = 0
    override fun onRender() {
        region.run {
            var crossCount = 0
            val futurePoints = points.drop(1) + points.first()
            val pointPairs = points zip futurePoints

            val renderingYCenter = player.location.y - (player.location.pitch * player.location.pitch / 200 * player.location.pitch.sign)

            pointPairs[times].let {
                for (i in (-5..6)) {
                    player.spawnParticle(
                        Particle.WAX_ON,
                        it.first.x,
                        i + renderingYCenter,
                        it.first.y,
                        1,
                        0.0,0.0,0.0,
                        0.0
                    )
                }

                val distance = (it.first distance it.second)

                val a = 36.0
                val x = min(distance,a/2)
                val result = -(x-a)*x*2/a

                it.first.splitBetweenVectorWith(it.second,result.toInt()).forEach { vector2D ->
                    listOf(-2,0,3).forEach { renderingY ->
                        player.spawnParticle(
                            Particle.WAX_OFF,
                            vector2D.x,
                            renderingY + renderingYCenter,
                            vector2D.y,
                            1,
                            0.0,0.0,0.0,
                            0.0
                        )
                    }
                }
            }
            times = (times + 1) % pointPairs.size
        }
    }
}