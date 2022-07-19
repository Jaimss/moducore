package dev.jaims.moducore.api.manager.player

import dev.jaims.moducore.api.manager.StorageManager
import org.bukkit.command.CommandSender
import java.util.*

interface NameManager {

    /**
     * Get a players name from their [uuid] - Sample shows a join listener that uses the [getName]
     * method.
     *
     * @param uuid the UUID of the [Player] whose name you want to get.
     *
     * @return the name of the player. Will use the players nickname if it is available as well as color codes
     * if the player is online and has permission. If the player is offline, simply the raw name data will be returned
     */
    suspend fun getName(uuid: UUID): String

    /**
     * Set a players nickname.
     *
     * @param uuid the uuid of the player whose nickname we are changing
     * @param nickName the new nickame or null to remove it
     * @param storageManager the storage manager
     * @param silent if a target player exists, they will recieve a message if this is true
     */
    suspend fun setNickName(
        uuid: UUID,
        nickName: String?,
        silent: Boolean,
        storageManager: StorageManager,
        executor: CommandSender? = null
    )
}