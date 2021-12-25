package io.github.muqhc.regionmaker

import io.github.muqhc.regionmaker.region.*

enum class RegionType(
    val gettingRegionFunction: (String) -> Region
) {
    Prism({ name ->  PrismRegion(name)},/*TODO : gettingRegionIOFunction(String)*/),
    Group({ name ->  GroupRegion(name)},/*TODO : gettingRegionIOFunction(String)*/)
}