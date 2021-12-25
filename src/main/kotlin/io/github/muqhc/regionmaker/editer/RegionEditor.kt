package io.github.muqhc.regionmaker.editer

import io.github.muqhc.regionmaker.EditFailureException
import io.github.muqhc.regionmaker.InvalidEditCommandException
import io.github.muqhc.regionmaker.plugin.RegionMakerPlugin
import io.github.muqhc.regionmaker.region.Region
import io.github.muqhc.regionmaker.render.RegionRenderer
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

abstract class RegionEditor<in SupportRegion : Region,in SupportRenderer : RegionRenderer<SupportRegion>>(
    open val plugin: Plugin
) : Listener {
    abstract val player: Player
    abstract val region: @UnsafeVariance SupportRegion
    abstract val renderer: @UnsafeVariance SupportRenderer

    abstract val commandRegex: Regex

    abstract val document: String

    open val renderingCycle = object : BukkitRunnable() {
        override fun run() {
            renderer.render()
        }
    }

    val promiseQueueWhenClickEvent: Queue<(PlayerInteractEvent) -> Unit> = LinkedList()

    fun getWholeDocument() = """
            =============================================================

            [ ${region::class.simpleName} Command Regex = $commandRegex ]

            ========= ${region::class.simpleName} Document =========

            $document

            =============================================================
        """.trimIndent()

    fun initialize() {
        renderer.initialize()
        renderingCycle.runTaskTimer(plugin, 0, 1)
        onInitialize()
    }

    fun orderCommand(command: String) {
        if (commandRegex.containsMatchIn(command)) {
            try {
                onCommand(command)
            } catch (exception: InvalidEditCommandException) {
                player.sendMessage(
                    getWholeDocument() + "\n\n${ChatColor.RED}${exception.message}"
                )
                throw exception
            } catch (exception: EditFailureException) {
                player.sendMessage(exception.message ?: "The Failed Command, \"$command\"")
                throw exception
            } catch (exception: Exception) {
                player.sendMessage(exception.message ?: "The Failed Command, \"$command\"")
                throw EditFailureException("[${exception::class.simpleName}]"+exception.message, exception)
            }
        } else {
            player.sendMessage(
                getWholeDocument() + "\n\n${ChatColor.RED}[ Your command doesn't match the Regex : $commandRegex ]"
            )
        }
    }

    fun disable() {
        renderingCycle.cancel()
        renderer.disable()
        onDisable()
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        promiseQueueWhenClickEvent.poll()(event)
        onClick(event)
    }

    open fun onInitialize() {}

    abstract fun onCommand(command: String)

    open fun onDisable() {}

    open fun onClick(event: PlayerInteractEvent) {}

}