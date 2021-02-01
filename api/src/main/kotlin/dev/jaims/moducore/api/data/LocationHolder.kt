package dev.jaims.moducore.api.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

/**
 * A Location Wrapper for Config Files
 *
 * @param worldName the name of the world
 * @param x the x coordinate
 * @param y the y coordinate
 * @param z the z coordinate
 * @param yaw the yaw
 * @param pitch the pitch
 */
data class LocationHolder(
    var worldName: String = "world",
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var yaw: Float = 0f,
    var pitch: Float = 0f
) {
    companion object {
        @JvmStatic
        fun from(location: Location): LocationHolder {
            return LocationHolder(location.world.name, location.x, location.y, location.z, location.yaw, location.pitch)
        }
    }

    /**
     * Get a [Location] from a location holder
     */
    val location: Location
        get() = Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch)

    val world: World
        get() = location.world
}
