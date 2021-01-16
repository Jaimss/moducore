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

enum class Perm(private val permString: String)
{

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
    CHAT_MK_DEFAULTCOLOR("moducore.chat.defaultcolor"),
    CHAT_MK_PURPLE("moducore.chat.color.purple"),
    CHAT_MK_MAGENTA("moducore.chat.color.magenta"),
    CHAT_MK_PINK("moducore.chat.color.pink"),
    CHAT_MK_CYAN("moducore.chat.color.cyan"),
    CHAT_MK_LIGHT_BLUE("moducore.chat.color.lightblue"),
    CHAT_MK_BLUE("moducore.chat.color.blue"),
    CHAT_MK_LIME("moducore.chat.color.lime"),
    CHAT_MK_GREEN("moducore.chat.color.green"),
    CHAT_MK_RED("moducore.chat.color.red"),
    CHAT_MK_ORANGE("moducore.chat.color.orange"),
    CHAT_MK_YELLOW("moducore.chat.color.yellow"),
    CHAT_MK_BROWN("moducore.chat.color.brown"),
    CHAT_MK_LIGHTGRAY("moducore.chat.color.lightgray"),
    CHAT_MK_GRAY("moducore.chat.color.gray"),
    CHAT_MK_BLACK("moducore.chat.color.black"),
    CHAT_MK_WHITE("moducore.chat.color.white"),

    SILENT_COMMAND("moducore.command.silent"),

    // ECONOMY
    BALANCE("moducore.command.balance"),
    BALANCE_TARGET("moducore.command.balance.others"),
    ECONOMY("moducore.command.economy"),

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

    // CLEAR INVENTORY
    CLEAR("moducore.command.clear"),
    CLEAR_OTHERS("moducore.command.clear.others"),

    // DISPOSE
    DISPOSE("moducore.command.dispose"),

    // FEED
    FEED("moducore.command.feed"),
    FEED_OTHERS("moducore.command.feed.others"),

    // FLIGHT
    FLY("moducore.command.fly"),
    FLY_OTHERS("moducore.command.fly.others"),

    // Give command
    GIVE("moducore.command.give"),
    GIVE_OTHERS("moducore.command.give.others"),

    // HEAL
    HEAL("moducore.command.heal"),
    HEAL_OTHERS("moducore.command.heal.others"),

    // DUMP
    DUMP("moducore.command.dump"),

    // NICKNAME
    NICKNAME("moducore.command.nickname"),
    NICKNAME_OTHERS("moducore.command.nickname.others"),
    UNNICK("moducore.command.unnick"),
    UNNICK_OTHERS("moducore.command.unnick.others"),

    // RELOAD
    RELOAD("moducore.command.reload"),

    // TPS
    TPS("moducore.command.tps"),

    // SIGN Commands
    SIGN_COMMANDS("moducore.command.runwithsigns");

    /**
     * @return true if they have the permission, false otherwise
     */
    fun has(player: CommandSender, sendNoPerms: Boolean = true): Boolean
    {
        if (player.hasPermission(ADMIN.permString) || player.hasPermission(this.permString))
            return true
        if (sendNoPerms) player.noPerms(this.permString)
        return false
    }

}