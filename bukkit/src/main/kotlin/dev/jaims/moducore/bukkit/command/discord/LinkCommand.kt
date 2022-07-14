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

package dev.jaims.moducore.bukkit.command.discord

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.discord.command.link.linkCodes
import dev.jaims.moducore.discord.config.DiscordBot
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LinkCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        if (!Permissions.DISCORD_LINK.has(sender)) return

        val inviteLink = plugin.api.discordFileManager.discord[DiscordBot.DISCORD_SERVER_INVITE_LINK]
        val code = getRandomCode()

        sender.send(Lang.DISCORD_LINK_CODE, null) {
            it.replace("{link}", inviteLink)
                .replace("{code}", code)
        }
        linkCodes[code] = sender.uniqueId
        // run the clear task
        val minutes = plugin.api.bukkitFileManager.config[Config.LINK_CODE_EXPIRE_TIME]
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Runnable {
            linkCodes.remove(code)
        }, (20 * 60L * minutes))
    }

    override val module: Property<Boolean> = Modules.DISCORD_BOT
    override val usage: String = "/link"
    override val description: String = "Link your discord and minecraft accounts."
    override val commandName: String = "link"

    private fun getRandomCode() = (1..6).joinToString("") {
        if (it % 2 == 0) getRandomLetter().toString()
        else getRandomNumber().toString()
    }

    private fun getRandomLetter() = (0..25).map { 'a'.plus(it) }.random()

    private fun getRandomNumber() = (0..9).random()

}