package dev.jaims.jcore.bukkit.manager

import org.bukkit.command.CommandSender

enum class Perm(private val permString: String) {

    // ADMIN PERM
    ADMIN("jcore.admin"),

    // FLIGHT
    FLY("jcore.command.fly"),
    FLY_TARGET("jcore.command.fly.others");

    /**
     * @return true if they have the permission, false otherwise
     */
    fun has(player: CommandSender, sendNoPerms: Boolean = true): Boolean {
        if (player.hasPermission(ADMIN.permString) || player.hasPermission(this.permString))
            return true
        if (sendNoPerms) player.noPerms()
        return false
    }

}