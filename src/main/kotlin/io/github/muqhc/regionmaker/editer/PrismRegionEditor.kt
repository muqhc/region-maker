package io.github.muqhc.regionmaker.editer

import io.github.muqhc.regionmaker.InvalidEditCommandException
import io.github.muqhc.regionmaker.plugin.RegionMakerPlugin
import io.github.muqhc.regionmaker.region.PrismRegion
import io.github.muqhc.regionmaker.render.PrismRegionRenderer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class PrismRegionEditor(
    plugin: Plugin,
    override val player: Player,
    override val region: PrismRegion,
    override val renderer: PrismRegionRenderer = PrismRegionRenderer(player, region)
) : RegionEditor<PrismRegion,PrismRegionRenderer>(plugin) {

    override val commandRegex: Regex = Regex(
        """^get points|(add|insert [0-9]+) (point((( | d| ~)[0-9]+(\.[0-9]+)?){2}|)|click point( [0-9]*[1-9][0-9]*)?)$"""
    )

    override val document: String = """
        |  Commands :
        |
        |    add point := add a point where you are as last point
        |    add point <x> <z> := add a point at (<x>,<z>) as last point
        |    add click point := add a point where you'll click as last point
        |    add click point <count> := add points <count> times where you'll click as last point
        |
        |    insert <index> point := add a point as <index>th at you
        |    insert <index> point <x> <z> := add a point as <index>th at (<x>,<z>)
        |    insert <index> click point := add a point as <index>th where you'll click
        |    insert <index> click point <count> := add a point as <index>th where you'll click <count> times
        |    
        |    remove near point := remove nearest point ${ ""/*TODO"Is Not Implemented Yet"*/ }
        |    remove point <index> := remove <index>th point ${ ""/*TODO"Is Not Implemented Yet"*/ }
        |    
        |    set center := set center node where you are ${ ""/*TODO"Is Not Implemented Yet"*/ }
        |    set center at near point := set center node at nearest point ${ ""/*TODO"Is Not Implemented Yet"*/ }
        |    set center at point <index> := set center node at <index>th point ${ ""/*TODO"Is Not Implemented Yet"*/ }
        |    set center <x> <z> := set center node at (<x>,<z>) ${ ""/*TODO"Is Not Implemented Yet"*/ }
        |    set click center := set center node where you'll click ${ ""/*TODO"Is Not Implemented Yet"*/ }
        |    
        |    get points := show a message, points of the region
        |    
        |  Parameters :
        |
        |    <x>,<z> :
        |      The decimal that means position x,z of a point you'll add.
        |      It can be written like '~(number)' or 'd(number)'.
        |        '~(number)' means delta number from your position.
        |        'd(number)' means delta number from the center of region you are editing.
        |
        |    <index> :
        |      The integer that means index of a point that you'll add or already existed.
        |      It is start at 1. ( first point is index 1 )
        |      
    """.trimIndent()

    private fun parseArgsTail(commandArgsTail: List<String>, willAddIndex: Int?) = when(commandArgsTail[0]){
        "point" -> when(commandArgsTail.count()){
            1 -> region.insertPoint(willAddIndex ?: region.points.count(), player.location.run { return@run x to z })
            3 -> {
                val x = when(commandArgsTail[1].first()){
                    'd' -> region.centerNode!!.x + commandArgsTail[1].drop(1).toDouble()
                    '~' -> player.location.x + commandArgsTail[1].drop(1).toDouble()
                    else -> commandArgsTail[1].toDouble()
                }
                val y = when(commandArgsTail[2].first()){
                    'd' -> region.centerNode!!.z + commandArgsTail[2].drop(1).toDouble()
                    '~' -> player.location.z + commandArgsTail[2].drop(1).toDouble()
                    else -> commandArgsTail[2].toDouble()
                }
                region.insertPoint(willAddIndex ?: region.points.count(), x, y)
            }
            else -> throw InvalidEditCommandException("Wrong args")
        }
        "click" -> when(commandArgsTail.count()){
            2 -> {
                promiseQueueWhenClickEvent.clear()
                promiseQueueWhenClickEvent += {
                    it.player.rayTraceBlocks(15.0)?.hitPosition?.run {
                        if (willAddIndex != null) region.insertPoint(willAddIndex, x, z)
                        else region.addPoint(x, z)
                    }
                }
            }
            3 -> {
                promiseQueueWhenClickEvent.clear()
                for (i in 1..commandArgsTail[2].toInt()) {
                    promiseQueueWhenClickEvent += {
                        it.player.rayTraceBlocks(15.0)?.hitPosition?.run {
                            if (willAddIndex != null) region.insertPoint(willAddIndex, x, z)
                            else region.addPoint(x, z)
                        }
                    }
                }
            }
            else -> throw InvalidEditCommandException("Wrong args")
        }
        else -> throw InvalidEditCommandException("Wrong args")
    }

    override fun onCommand(command: String) {
        val commandArgs = command.split(' ')
        when(commandArgs[0]){
            "add" -> parseArgsTail(commandArgs.drop(1), null)
            "insert" -> parseArgsTail(commandArgs.drop(2), commandArgs[1].toInt()-1)
            "get" -> region.points.forEach { player.sendMessage("(${it.x},${it.y})") }
        }
    }



}