package io.github.muqhc.regionmaker.manager

import io.github.muqhc.regionmaker.region.Region

class DefaultRegionsManager : RegionsManager {
    override val regions: MutableMap<String,Region> = mutableMapOf()

}