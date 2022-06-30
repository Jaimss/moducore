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

package dev.jaims.moducore.discord.api

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.api.manager.DiscordManager
import dev.jaims.moducore.discord.ModuCoreDiscordBot
import dev.jaims.moducore.discord.command.DiscordCommand
import dev.jaims.moducore.discord.command.economy.BalanceSlashDiscordCommand
import dev.jaims.moducore.discord.command.economy.PaySlashDiscordCommand
import dev.jaims.moducore.discord.command.link.LinkSlashDiscordCommand
import dev.jaims.moducore.discord.command.util.info.InfoSlashDiscordCommand
import dev.jaims.moducore.discord.command.util.info.InfoUserDiscordCommand
import dev.jaims.moducore.discord.config.DiscordBot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent

class DefaultDiscordManager(val fileManager: DiscordFileManager) : DiscordManager {

    override lateinit var jda: JDA

    fun startJda(bot: ModuCoreDiscordBot, api: ModuCoreAPI) {

        jda = JDABuilder.create(fileManager.discord[DiscordBot.TOKEN], GatewayIntent.GUILD_MEMBERS)
            .apply {
                // activity if the want it
                val activityTypeString = fileManager.discord[DiscordBot.ACTIVITY_TYPE]
                if (activityTypeString != "none") {
                    val activityType = Activity.ActivityType.valueOf(activityTypeString.uppercase())
                    val activity = fileManager.discord[DiscordBot.ACTIVITY_DESCRIPTION]
                    val streamUrl = fileManager.discord[DiscordBot.ACTIVITY_STREAM_URL]
                    setActivity(Activity.of(activityType, activity, streamUrl))
                }
            }
            .build()
            .awaitReady()

        registerCommands(bot, api)
        registerListeners(bot, api)

    }

    private fun registerCommands(bot: ModuCoreDiscordBot, api: ModuCoreAPI) {
        val commands = listOf(
            InfoSlashDiscordCommand(bot, api),
            InfoUserDiscordCommand(bot, api),

            LinkSlashDiscordCommand(bot, api),

            BalanceSlashDiscordCommand(bot, api),
            PaySlashDiscordCommand(bot, api)
        )
        jda.updateCommands().addCommands(commands.map(DiscordCommand::commandData)).queue()
        for (command in commands) jda.addEventListener(command)
    }

    private fun registerListeners(bot: ModuCoreDiscordBot, api: ModuCoreAPI) {
        // TODO
    }

}