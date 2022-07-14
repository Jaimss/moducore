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

package dev.jaims.moducore.discord.command.util.info

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.discord.ModuCoreDiscordBot
import dev.jaims.moducore.discord.command.UserDiscordCommand
import dev.jaims.moducore.discord.config.DiscordBot
import dev.jaims.moducore.discord.config.DiscordModules
import me.mattstudios.config.properties.Property
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class InfoUserDiscordCommand(override val bot: ModuCoreDiscordBot, override val api: ModuCoreAPI) :
    UserDiscordCommand(bot, api) {
    override fun UserContextInteractionEvent.handle() {
        deferReply(bot.fileManager.discord[DiscordBot.EPHEMERAL_INFO]).queue()

        val message = getInfoMessage(
            target,
            bot.fileManager.discordLang,
            bot.api.storageManager,
            bot.nameFormatManager,
            bot.api.playerManager
        )

        hook.sendMessage(message).queue()
    }

    override val name: String = "User Information"
    override val commandData: CommandData = Commands.user(name)
    override val module: Property<Boolean> = DiscordModules.COMMAND_INFO
}