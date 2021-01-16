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

package dev.jaims.moducore.bukkit

import dev.jaims.mcutils.bukkit.KotlinPlugin
import dev.jaims.mcutils.bukkit.util.log
import dev.jaims.moducore.bukkit.api.DefaultModuCoreAPI
import dev.jaims.moducore.bukkit.command.*
import dev.jaims.moducore.bukkit.command.economy.BalanceCommand
import dev.jaims.moducore.bukkit.command.economy.EconomyCommand
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeAdventure
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeCreative
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeSpectator
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeSurvival
import dev.jaims.moducore.bukkit.command.nickname.NicknameCommand
import dev.jaims.moducore.bukkit.command.nickname.NicknameRemoveCommand
import dev.jaims.moducore.bukkit.command.repair.Repair
import dev.jaims.moducore.bukkit.command.repair.RepairAll
import dev.jaims.moducore.bukkit.command.speed.FlySpeedCommand
import dev.jaims.moducore.bukkit.command.speed.SpeedCommand
import dev.jaims.moducore.bukkit.command.speed.WalkSpeedCommand
import dev.jaims.moducore.bukkit.command.teleport.TeleportCommand
import dev.jaims.moducore.bukkit.command.teleport.TeleportHereCommand
import dev.jaims.moducore.bukkit.command.teleport.TeleportPlayerToPlayerCommand
import dev.jaims.moducore.bukkit.command.teleport.TeleportPositionCommand
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.event.listener.*
import dev.jaims.moducore.bukkit.external.ModuCorePlaceholderExpansion
import dev.jaims.moducore.bukkit.util.getLatestVersion
import dev.jaims.moducore.bukkit.util.serverStartTime
import java.util.*
import javax.print.attribute.standard.Severity

class ModuCore : KotlinPlugin()
{

    lateinit var api: DefaultModuCoreAPI

    override fun enable()
    {
        if (!isPaper())
        {
            """We detected you are not using PaperSpigot. Moducore will continue to run, however this will likely cause errors in the future. 
               Please swap your jar for the latest paper found at https://papermc.io/downloads. Thanks.
            """.trimIndent().log(Severity.ERROR)
        }

        notifyVersion()

        ModuCorePlaceholderExpansion(this).register()
        api.vaultEconomyProvider.register()

        serverStartTime = Date()
    }

    override fun disable()
    {
        // save player data
        api.storageManager.updateTask.cancel()
        api.storageManager.saveAllData(api.storageManager.playerDataCache)

        // unregister vault
        api.vaultEconomyProvider.unregister()

        // unregister the api
        api.unregisterServiceProvider()
    }

    /**
     * Check the latest version and alert the servers console if it isn't the latest.
     */
    private fun notifyVersion()
    {
        val latestVersion = getLatestVersion(86911)
        if (latestVersion != null && latestVersion != description.version)
        {
            "There is a new version of ModuCore Available ($latestVersion)! Please download it from https://www.spigotmc.org/resources/86911/"
                .log(Severity.WARNING)
        }
    }

    override fun registerCommands()
    {

        val modules = this.api.fileManager.modules

        // add a list of elements
        fun <T> MutableList<T>.addMultiple(vararg element: T): MutableList<T>
        {
            element.forEach {
                add(it)
            }
            return this
        }

        if (modules.getProperty(Modules.ECONOMY)) allCommands.addMultiple(
            BalanceCommand(this),
            EconomyCommand(this)
        )
        if (modules.getProperty(Modules.COMMAND_GAMEMODE)) allCommands.addMultiple(
            GamemodeAdventure(this),
            GamemodeCreative(this),
            GamemodeSpectator(this),
            GamemodeSurvival(this)
        )
        if (modules.getProperty(Modules.COMMAND_NICKNAME)) allCommands.addMultiple(
            NicknameCommand(this),
            NicknameRemoveCommand(this)
        )
        if (modules.getProperty(Modules.COMMAND_REPAIR)) allCommands.addMultiple(
            Repair(this),
            RepairAll(this)
        )
        if (modules.getProperty(Modules.COMMAND_SPEED)) allCommands.addMultiple(
            FlySpeedCommand(this),
            SpeedCommand(this),
            WalkSpeedCommand(this)
        )
        if (modules.getProperty(Modules.COMMAND_TELEPORT)) allCommands.addMultiple(
            TeleportCommand(this),
            TeleportHereCommand(this),
            TeleportPlayerToPlayerCommand(this),
            TeleportPositionCommand(this)
        )
        if (modules.getProperty(Modules.COMMAND_CLEARINVENTORY)) allCommands.add(ClearInventoryCommand(this))
        if (modules.getProperty(Modules.COMMAND_DISPOSE)) allCommands.add(DisposeCommand(this))
        if (modules.getProperty(Modules.COMMAND_FEED)) allCommands.add(FeedCommand(this))
        if (modules.getProperty(Modules.COMMAND_FLY)) allCommands.add(FlyCommand(this))
        if (modules.getProperty(Modules.COMMAND_GIVE)) allCommands.add(GiveCommand(this))
        if (modules.getProperty(Modules.COMMAND_HEAL)) allCommands.add(HealCommand(this))
        if (modules.getProperty(Modules.COMMAND_HELP)) allCommands.add(HelpCommand(this))
        if (modules.getProperty(Modules.COMMAND_TPS)) allCommands.add(TicksPerSecondCommand(this))
        allCommands.add(ReloadCommand(this))

        allCommands.forEach {
            it.register(this)
        }
    }

    override fun registerListeners()
    {
        register(
            SignChangeListener(this),
            PlayerChatListener(this),
            PlayerInteractListener(this),
            PlayerJoinListener(this),
            PlayerQuitListener(this)
        )

    }

    override fun registerManagers()
    {
        api = DefaultModuCoreAPI(this)
    }

    private fun isPaper(): Boolean
    {
        return try
        {
            Class.forName("com.destroystokyo.paper.VersionHistoryManager\$VersionData")
            true
        } catch (e: ClassNotFoundException)
        {
            false
        }
    }

}