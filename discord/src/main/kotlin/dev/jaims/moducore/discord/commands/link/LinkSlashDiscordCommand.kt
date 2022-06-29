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

package dev.jaims.moducore.discord.commands.link

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.discord.ModuCoreDiscordBot
import dev.jaims.moducore.discord.commands.SlashDiscordCommand
import kotlinx.coroutines.runBlocking
import me.mattstudios.config.properties.Property
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import java.util.*

val linkCodes = mutableMapOf<String, UUID>()

class LinkSlashDiscordCommand(
    override val bot: ModuCoreDiscordBot,
    override val api: ModuCoreAPI
) : SlashDiscordCommand() {

    override val name: String = "link"
    override val description: String = "Link your Discord and Minecraft Accounts"
    override val commandData: CommandData = Commands.slash(name, description)
        .addOption(OptionType.STRING, "code", "The code that typing /link in Minecraft gives you.", true)
    override val module: Property<Boolean>? = null

    override fun SlashCommandInteractionEvent.handle() {
        deferReply(true).queue()
        val code = getOption("code")!!.asString

        val linkedUUID = linkCodes[code] ?: run {
            val errorMessage =
                bot.fileManager.discordLang.linkCodeInvalid.asDiscordMessage()
            hook.sendMessage(errorMessage).queue()
            return
        }

        val playerData = runBlocking { api.storageManager.getPlayerData(linkedUUID) }
        playerData.discordID = user.idLong

        val successMessage = bot.fileManager.discordLang.linkSuccess.asDiscordMessage(
            embedDescriptionModifier = { it.replace("{uuid}", linkedUUID.toString()) }
        )
        hook.sendMessage(successMessage).queue()
    }

}