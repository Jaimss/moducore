/*
 * This file is a part of JCore, licensed under the MIT License.
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

package dev.jaims.jcore.bukkit.manager

import dev.jaims.jcore.bukkit.util.noPerms
import org.bukkit.command.CommandSender

enum class Perm(private val permString: String)
{

    // ADMIN PERM
    ADMIN("jcore.admin"),

    // CHAT FORMAT
    CHAT_MK_BOLD("jcore.chat.markdown.bold"),
    CHAT_MK_ITALIC("jcore.chat.markdown.italic"),
    CHAT_MK_STRIKETHROUGH("jcore.chat.markdown.strikethrough"),
    CHAT_MK_UNDERLINE("jcore.chat.markdown.underline"),
    CHAT_MK_OBFUSCATED("jcore.chat.markdown.obfuscated"),
    CHAT_MK_COLOR("jcore.chat.markdown.color"),
    CHAT_MK_HEX("jcore.chat.color.hex"),
    CHAT_MK_GRADIENT("jcore.chat.color.gradient"),
    CHAT_MK_RAINBOW("jcore.chat.color.rainbow"),
    CHAT_MK_ACTIONS("jcore.chat.markdown.actions"), // mod+ only!! gives ability to run commands as players and stuff

    // use default colors (COMING SOON)
    CHAT_MK_DEFAULTCOLOR("jcore.chat.defaultcolor"),
    CHAT_MK_PURPLE("jcore.chat.color.purple"),
    CHAT_MK_MAGENTA("jcore.chat.color.magenta"),
    CHAT_MK_PINK("jcore.chat.color.pink"),
    CHAT_MK_CYAN("jcore.chat.color.cyan"),
    CHAT_MK_LIGHT_BLUE("jcore.chat.color.lightblue"),
    CHAT_MK_BLUE("jcore.chat.color.blue"),
    CHAT_MK_LIME("jcore.chat.color.lime"),
    CHAT_MK_GREEN("jcore.chat.color.green"),
    CHAT_MK_RED("jcore.chat.color.red"),
    CHAT_MK_ORANGE("jcore.chat.color.orange"),
    CHAT_MK_YELLOW("jcore.chat.color.yellow"),
    CHAT_MK_BROWN("jcore.chat.color.brown"),
    CHAT_MK_LIGHTGRAY("jcore.chat.color.lightgray"),
    CHAT_MK_GRAY("jcore.chat.color.gray"),
    CHAT_MK_BLACK("jcore.chat.color.black"),
    CHAT_MK_WHITE("jcore.chat.color.white"),

    // Gamemode
    GAMEMODE_SURVIVAL("jcore.command.gamemode.survival"),
    GAMEMODE_CREATIVE("jcore.command.gamemode.creative"),
    GAMEMODE_ADVENTURE("jcore.command.gamemode.adventure"),
    GAMEMODE_SPECTATOR("jcore.command.gamemode.spectator"),
    GAMEMODE_SURVIVAL_TARGET("jcore.command.gamemode.survival.target"),
    GAMEMODE_CREATIVE_TARGET("jcore.command.gamemode.creative.target"),
    GAMEMODE_ADVENTURE_TARGET("jcore.command.gamemode.adventure.target"),
    GAMEMODE_SPECTATOR_TARGET("jcore.command.gamemode.spectator.target"),

    // REPAIR
    REPAIR("jcore.command.repair"),
    REPAIR_OTHERS("jcore.command.repairothers"),
    REPAIR_ALL("jcore.command.repairall"),
    REPAIR_ALL_OTHERS("jcore.command.repairall.others"),

    // CLEAR INVENTORY
    CLEAR("jcore.command.clear"),
    CLEAR_OTHERS("jcore.command.clear.others"),

    // DISPOSE
    DISPOSE("jcore.command.dispose"),

    // FEED
    FEED("jcore.command.feed"),
    FEED_OTHERS("jcore.command.feed.others"),

    // FLIGHT
    FLY("jcore.command.fly"),
    FLY_OTHERS("jcore.command.fly.others"),

    // Give command
    GIVE("jcore.command.give"),
    GIVE_OTHERS("jcore.command.give.others"),

    // HEAL
    HEAL("jcore.command.heal"),
    HEAL_OTHERS("jcore.command.heal.others"),

    // RELOAD
    RELOAD("jcore.command.reload"),

    // SIGN Commands
    SIGN_COMMANDS("jcore.command.runwithsigns");

    /**
     * @return true if they have the permission, false otherwise
     */
    fun has(player: CommandSender, sendNoPerms: Boolean = true): Boolean
    {
        if (player.hasPermission(ADMIN.permString) || player.hasPermission(this.permString))
            return true
        if (sendNoPerms) player.noPerms()
        return false
    }

}