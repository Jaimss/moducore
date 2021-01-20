package dev.jaims.moducore.api.manager

interface HologramManager {

    /**
     * Create a hologram. Will generate it, add it to the storage and spawn it at the location given.
     *
     * @param hologram the hologram to create.
     */
    fun createHologram(hologram: Hologram)

    /**
     * Get a hologram from the storage.
     *
     * @param name case insensitive name of the hologram
     *
     * @return the [Hologram]
     */
    fun getHologram(name: String): Hologram

    /**
     * Delete a hologram. Will delete it from the storage as well as despawn it.
     *
     * @param hologram the hologram to delete
     */
    fun deleteHologram(hologram: Hologram)

    /**
     * Update a hologram.
     *
     * @param hologram the hologram to update
     */
    fun updateHologram(hologram: Hologram)

}

/**
 * Data class for a hologram. Everything is 0 indexed here. Current page 0 is the first, 1 is the second page, etc.
 *
 * @param pages the list of [HologramPage]s. Size is the total pages.
 * @param location the location of the hologram
 * @param currentPage the current page
 */
data class Hologram(
    val name: String,
    val pages: List<HologramPage>,
    val location: LocationHolder,
    val currentPage: Int
)

/**
 * Data class for a Hologram Page
 *
 * @param lines a list of strings one for each line
 */
data class HologramPage(
    val lines: List<String>,
)

