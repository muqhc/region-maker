package io.github.muqhc.regionmaker.render

import io.github.muqhc.regionmaker.region.Region
import org.bukkit.entity.Player

abstract class RegionRenderer<in SupportRegion : Region> {

    abstract val player: Player
    abstract val region: @UnsafeVariance SupportRegion

    fun initialize() {
        onInitialize()
    }

    fun render() {
        onRender()
    }

    fun disable() {
        onDisable()
    }

    open fun onInitialize() {}

    abstract fun onRender()

    open fun onDisable() {}
}