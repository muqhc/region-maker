package io.github.muqhc.regionmaker.command

import io.github.monun.kommand.StringType
import io.github.monun.kommand.kommand
import io.github.muqhc.regionmaker.RegionType
import io.github.muqhc.regionmaker.editer.RegionEditor
import io.github.muqhc.regionmaker.manager.EditingManager
import io.github.muqhc.regionmaker.manager.RegionsManager
import io.github.muqhc.regionmaker.plugin.RegionMakerPlugin
import io.github.muqhc.regionmaker.region.Region
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

internal object KommandRegionMaker {
    private fun addRegion(name: String, region: (String) -> Region, regionsManager: RegionsManager) =
        regionsManager.addRegion(name, region(name))

    private fun addEditor(plugin: Plugin, player: Player, region: Region, editingManager: EditingManager) =
        editingManager.addEditor(player, region.getDefaultSupportingEditor(plugin, player))

    private fun removeRegion(region: Region, regionsManager: RegionsManager) =
        regionsManager.removeRegion(region)

    private fun orderEditorCommand(command: String, editor: RegionEditor<*,*>) =
        editor.orderCommand(command.lowercase())

    private fun checkIsInside(region: Region, entities: Collection<Entity>): Boolean {
        entities.forEach {
            if (!region.isInside(it)) return false
        }
        return true
    }


    fun register(plugin: RegionMakerPlugin, regionsManager: RegionsManager, editingManager: EditingManager) {

        plugin.run {

            kommand {

                register("region"){
                    val regionTypesMap = RegionType.values().associate { it.name.lowercase() to it.gettingRegionFunction }
                    val regionTypeMapArgument = dynamicByMap(regionTypesMap)

                    val regionArgument = dynamic { _, input ->
                        regionsManager.getRegion(input)
                    }.apply {
                        suggests {
                            suggest(regionsManager.getNames())
                        }
                    }

                    then("make"){
                        requires { playerOrNull != null }
                        then("type" to regionTypeMapArgument,"name" to string()){
                            executes {
                                addRegion(it["name"], it["type"], regionsManager)
                                editingManager.removeEditor(player.uniqueId)
                                addEditor(plugin, player, regionsManager.getRegion(it["name"])!!, editingManager)
                                regionsManager.getRegion(it["name"])?.run {
                                    sender.sendMessage("${ChatColor.AQUA}(${this::class.simpleName})\"${name}\" is created and being edited by you")
                                }
                            }
                        }
                    }

                    then("create"){
                        then("type" to regionTypeMapArgument,"name" to string()){
                            executes {
                                addRegion(it["name"], it["type"], regionsManager)
                                regionsManager.getRegion(it["name"])?.run {
                                    sender.sendMessage("${ChatColor.AQUA}(${this::class.simpleName})\"${name}\" is created")
                                }
                            }
                        }
                    }

                    then("edit"){
                        requires { playerOrNull != null }
                        then("target-region" to regionArgument){
                            executes {
                                editingManager.removeEditor(player.uniqueId)
                                addEditor(plugin, player, it["target-region"], editingManager)
                            }
                        }
                    }

                    then("list"){
                        executes {
                            sender.sendMessage(
                                regionsManager.getRegionsAsImmutable().map {
                                    "${it.key} : ${it.value::class.simpleName}"
                                }.joinToString("\n","\n")
                            )
                        }
                    }

                    then("inside"){
                        then("region" to regionArgument, "entities" to entities()){
                            executes {
                                if (checkIsInside(it["region"], it["entities"]))
                                    sender.sendMessage("True")
                                else
                                    sender.sendMessage("False")
                            }
                        }
                    }

                    then("remove"){
                        then("name" to regionArgument){
                            executes {
                                removeRegion(it["name"], regionsManager)
                            }
                        }
                    }
                }

                register("editor"){
                    requires { (playerOrNull != null) }

                    then("run"){
                        then("editor-command" to string(StringType.GREEDY_PHRASE)) {
                            executes {
                                val editor = player.getUsingEditor() ?: run {
                                    player.sendMessage("${ChatColor.RED}[ You're not editing a region ]")
                                    return@executes
                                }
                                orderEditorCommand(it["editor-command"], editor)
                            }
                        }
                    }
                    then("doc"){
                        executes {
                            val editor = player.getUsingEditor() ?: run {
                                player.sendMessage("${ChatColor.RED}[ You're not editing a region ]")
                                return@executes
                            }
                            player.sendMessage(editor.getWholeDocument())
                        }
                    }
                    then("break"){
                        executes {
                            editingManager.removeEditor(player.uniqueId)
                        }
                    }
                }
            }

        }

    }
}