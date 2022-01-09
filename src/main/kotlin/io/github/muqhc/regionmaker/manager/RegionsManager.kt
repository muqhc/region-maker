package io.github.muqhc.regionmaker.manager

import io.github.muqhc.regionmaker.region.Region
import io.github.muqhc.runction.*
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import java.io.Serializable
import java.io.ObjectOutputStream
import java.io.ObjectInputStream
import java.util.*

interface RegionsManager : Serializable {
    val regions: MutableMap<String,Region>

    fun addRegion(name: String, region: Region) {
        regions[name] = region
    }

    fun getRegionsAsImmutable() = regions.toMap()
    fun getNames() = regions.keys.toSet()
    fun getRegion(name: String) = regions[name]

    fun removeRegion(name: String) = regions.remove(name)
    fun removeRegion(region: Region) = regions.remove(region.name)

    fun serialize(): String = ByteArrayOutputStream().apply {
        ObjectOutputStream(this).writeObject(this@RegionsManager)
    }.toByteArray() bind Base64.getEncoder()::encodeToString

    fun deserialize(base64String: String): RegionsManager = ByteArrayInputStream(
        base64String bind Base64.getDecoder()::decode
    ).bindOn { ObjectInputStream(this).readObject() } as RegionsManager

}