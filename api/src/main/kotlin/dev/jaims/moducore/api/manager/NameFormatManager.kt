package dev.jaims.moducore.api.manager

import net.dv8tion.jda.api.entities.User

interface NameFormatManager {

    /**
     * @return the linked user formatted string. if the user is not linked, this will just return the unlinked format
     */
    suspend fun getFormatted(user: User): String

}