package io.github.muqhc.regionmaker.manager

import io.github.muqhc.regionmaker.region.Region

interface RegionsManager {
    val regions: MutableMap<String,Region>

    fun addRegion(name: String, region: Region) {
        regions[name] = region
    }

    fun getRegionsAsImmutable() = regions.toMap()
    fun getNames() = regions.keys.toSet()
    fun getRegion(name: String) = regions[name]

    fun removeRegion(name: String) = regions.remove(name)
    fun removeRegion(region: Region) = regions.remove(region.name)
}