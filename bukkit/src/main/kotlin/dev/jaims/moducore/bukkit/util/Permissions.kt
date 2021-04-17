/*
 * This file is a part of ModuCore, licensed under the MIT License.
 *
 * Copyright (c) 2020 James Harrell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaims.moducore.bukkit.util

import org.bukkit.command.CommandSender

enum class Permissions(val permString: String) {

    // ADMIN PERM
    ADMIN("moducore.admin"),

    // CHAT FORMAT
    CHAT_MK_BOLD("moducore.chat.markdown.bold"),
    CHAT_MK_ITALIC("moducore.chat.markdown.italic"),
    CHAT_MK_STRIKETHROUGH("moducore.chat.markdown.strikethrough"),
    CHAT_MK_UNDERLINE("moducore.chat.markdown.underline"),
    CHAT_MK_OBFUSCATED("moducore.chat.markdown.obfuscated"),
    CHAT_MK_COLOR("moducore.chat.markdown.color"),
    CHAT_MK_HEX("moducore.chat.color.hex"),
    CHAT_MK_GRADIENT("moducore.chat.color.gradient"),
    CHAT_MK_RAINBOW("moducore.chat.color.rainbow"),
    CHAT_MK_ACTIONS("moducore.chat.markdown.actions"), // mod+ only!! gives ability to run commands as players and stuff

    // use default colors (COMING SOON)
    CC_CUSTOM("moducore.chat.color.custom"),
    CC_PURPLE("moducore.chat.color.purple"),
    CC_MAGENTA("moducore.chat.color.magenta"),
    CC_PINK("moducore.chat.color.pink"),
    CC_CYAN("moducore.chat.color.cyan"),
    CC_LIGHTBLUE("moducore.chat.color.lightblue"),
    CC_BLUE("moducore.chat.color.blue"),
    CC_AQUA("moducore.chat.color.aqua"),
    CC_LIME("moducore.chat.color.lime"),
    CC_GREEN("moducore.chat.color.green"),
    CC_RED("moducore.chat.color.red"),
    CC_ORANGE("moducore.chat.color.orange"),
    CC_YELLOW("moducore.chat.color.yellow"),
    CC_BROWN("moducore.chat.color.brown"),
    CC_LIGHTGRAY("moducore.chat.color.lightgray"),
    CC_GRAY("moducore.chat.color.gray"),
    CC_BLACK("moducore.chat.color.black"),
    CC_WHITE("moducore.chat.color.white"),

    SILENT_COMMAND("moducore.command.silent"),

    BYPASS_COOLDOWN("moducore.command.cooldown.bypass"),

    SUDO("moducore.command.sudo"),

    // ECONOMY
    BALANCE("moducore.command.balance"),
    BALANCE_TARGET("moducore.command.balance.others"),
    ECONOMY("moducore.command.economy"),
    PAY("moducore.command.pay"),

    // Gamemode
    GAMEMODE_SURVIVAL("moducore.command.gamemode.survival"),
    GAMEMODE_CREATIVE("moducore.command.gamemode.creative"),
    GAMEMODE_ADVENTURE("moducore.command.gamemode.adventure"),
    GAMEMODE_SPECTATOR("moducore.command.gamemode.spectator"),
    GAMEMODE_SURVIVAL_TARGET("moducore.command.gamemode.survival.target"),
    GAMEMODE_CREATIVE_TARGET("moducore.command.gamemode.creative.target"),
    GAMEMODE_ADVENTURE_TARGET("moducore.command.gamemode.adventure.target"),
    GAMEMODE_SPECTATOR_TARGET("moducore.command.gamemode.spectator.target"),

    // REPAIR
    REPAIR("moducore.command.repair"),
    REPAIR_OTHERS("moducore.command.repairothers"),
    REPAIR_ALL("moducore.command.repairall"),
    REPAIR_ALL_OTHERS("moducore.command.repairall.others"),

    // SPAWN
    SET_SPAWN("moducore.command.setspawn"),
    SPAWN("moducore.command.spawn"),
    SPAWN_OTHERS("moducore.command.spawn_others"),

    // SPEED
    FLYSPEED("moducore.command.flyspeed"),
    FLYSPEED_OTHERS("moducore.command.flyspeed.others"),
    WALKSPEED("moducore.command.walkspeed"),
    WALKSPEED_OTHERS("moducore.command.walkspeed.others"),

    // TELEPORT
    TELEPORT("moducore.command.teleport"),
    TELEPORT_POS("moducore.command.teleport_pos"),
    TELEPORT_PLAYER_TO_PLAYER("moducore.command.teleport_player_to_player"),
    TELEPORT_HERE("moducore.command.teleport_here"),
    TELEPORT_RANDOM("moducore.command.random_teleport"),
    TELEPORT_RANDOM_OTHERS("moducore.command.random_teleport.others"),
    TELEPORT_REQUEST("moducore.command.teleportrequest.teleportrequest"),
    TELEPORT_REQUEST_BYPASSCOOLDOWN("moducore.command.teleportrequest.bypasscooldown"),
    TELEPORT_ACCEPT("moducore.command.teleportrequest.teleportaccept"),
    TELEPORT_DENY("moducore.command.teleportrequest.teleportdeny"),

    // WARP
    LIST_WARPS("moducore.command.list_warps"),
    SET_WARP("moducore.command.setwarp"),
    DEL_WARP("moducore.command.delwarp"),
    WARP_NAME("moducore.command.warp.<name>"),
    WARP_OTHERS("moducore.command.warp.<name>.others"),

    // BROADCAST
    BROADCAST("moducore.command.broadcast"),

    // CHATCOLOR
    CHATCOLOR("moducore.command.chatcolor"),

    // CLEAR INVENTORY
    CLEAR("moducore.command.clear"),
    CLEAR_OTHERS("moducore.command.clear.others"),

    // CRAFT
    CRAFT("moducore.command.craft"),

    // DISPOSE
    DISPOSE("moducore.command.dispose"),

    // ENDERCHEST
    ENDERCHEST("moducore.command.enderchest"),
    ENDERCHEST_OTHERS("moducore.command.enderchest.others"),
    ENDERCHEST_OTHERS_MODIFY("moducore.command.enderchest.others.modify"),

    // FEED
    FEED("moducore.command.feed"),
    FEED_OTHERS("moducore.command.feed.others"),

    // FLIGHT
    FLY("moducore.command.fly"),
    FLY_OTHERS("moducore.command.fly.others"),

    // Give command
    GIVE("moducore.command.give"),
    GIVE_OTHERS("moducore.command.give.others"),

    // HAT
    HAT("moducore.command.hat"),

    // HEAL
    HEAL("moducore.command.heal"),
    HEAL_OTHERS("moducore.command.heal.others"),

    // INVSEE
    INVSEE("moducore.command.invsee"),

    // HOLOGRAM
    HOLOGRAM("moducore.command.hologram"),

    // HOME
    SET_HOME_AMOUNT("moducore.command.sethome.<amount>"),
    HOMES("moducore.command.homes"),
    HOMES_OTHERS("moducore.command.homes.others"),
    HOME("moducore.command.home"),
    HOME_OTHERS("moducore.command.home.others"),
    DELHOME("moducore.command.delhome"),
    DELHOME_OTHERS("moducore.command.delhome.others"),

    // ITEMMETA
    RENAME("moducore.command.rename"),
    RENAME_FORMAT_AND_COLOR("moducore.command.rename.withformatsandcolor"),
    SET_LORE("moducore.command.setlore"),
    SET_LORE_FORMAT_AND_COLOR("moducore.command.setlore.withformatsandcolor"),

    // KITS
    CREATE_KIT("moducore.command.createkit"),
    DELETE_KIT("moducore.command.deletekit"),
    USE_KIT("moducore.command.kit.<kitname>"),
    USE_KIT_OTHERS("moducore.command.kit.others.<kitname>"),
    USE_KIT_BYPASS_COOLDOWN("moducore.kit.bypasscooldown.<kitname>"),

    // LOCKDOWN
    LOCKDOWN("moducore.command.lockdown"),
    JOIN_LOCKDOWN_GENERAL("moducore.lockdown.join.<group>"),

    // DUMP
    DUMP("moducore.command.dump"),

    // PING
    PING("moducore.command.ping"),
    PING_OTHERS("moducore.command.ping.others"),

    // NICKNAME
    NICKNAME("moducore.command.nickname"),
    NICKNAME_OTHERS("moducore.command.nickname.others"),
    UNNICK("moducore.command.unnick"),
    UNNICK_OTHERS("moducore.command.unnick.others"),

    // PMs
    PRIVATE_MESSAGE_SEND("moducore.command.privatemessage"),
    SOCIAL_SPY("moducore.command.socialspy"),

    // RELOAD
    RELOAD("moducore.command.reload"),

    // MORE
    MORE("moducore.command.more"),

    // NEAR
    NEAR("moducore.command.near"),

    // TPS
    TPS("moducore.command.tps"),

    // TOP
    TOP("moducore.command.top"),

    // TIME & Weather
    TIME("moducore.command.time"),
    WEATHER("moducore.command.weather"),
    PTIME("moducore.command.ptime"),
    PWEATHER("moducore.command.pweather"),

    // SIGN Commands
    SIGN_COMMANDS("moducore.command.runwithsigns");

    /**
     * @return true if they have the permission, false otherwise
     */
    fun has(player: CommandSender, sendNoPerms: Boolean = true, transform: (String) -> String = { it }): Boolean {
        val perm = transform(permString)
        if (player.hasPermission(ADMIN.permString) || player.hasPermission(perm))
            return true
        if (sendNoPerms) player.noPerms(perm)
        return false
    }

    /**
     * @return the amount of whatever permission they can have, or null if they can't have any.
     */
    fun getAmount(player: CommandSender, sendNoPerms: Boolean = false): Int? {
        if (player.hasPermission(ADMIN.permString)) return Int.MAX_VALUE
        for (num in 0..100) {
            if (player.hasPermission(this.permString.replace("<amount>", num.toString()))) {
                return num
            }
        }
        if (sendNoPerms) {
            player.noPerms(this.permString.replace("<amount>", "1"))
        }
        return null
    }

}