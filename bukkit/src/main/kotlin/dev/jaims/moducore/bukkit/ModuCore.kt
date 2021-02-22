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
import dev.jaims.moducore.bukkit.api.DefaultModuCoreAPI
import dev.jaims.moducore.bukkit.command.*
import dev.jaims.moducore.bukkit.command.economy.BalanceCommand
import dev.jaims.moducore.bukkit.command.economy.EconomyCommand
import dev.jaims.moducore.bukkit.command.economy.PayCommand
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeAdventure
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeCreative
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeSpectator
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeSurvival
import dev.jaims.moducore.bukkit.command.hologram.HologramCommand
import dev.jaims.moducore.bukkit.command.home.DelhomeCommand
import dev.jaims.moducore.bukkit.command.home.HomeCommand
import dev.jaims.moducore.bukkit.command.home.HomesCommand
import dev.jaims.moducore.bukkit.command.home.SethomeCommand
import dev.jaims.moducore.bukkit.command.nickname.NicknameCommand
import dev.jaims.moducore.bukkit.command.nickname.NicknameRemoveCommand
import dev.jaims.moducore.bukkit.command.repair.Repair
import dev.jaims.moducore.bukkit.command.repair.RepairAll
import dev.jaims.moducore.bukkit.command.spawn.SetSpawnCommand
import dev.jaims.moducore.bukkit.command.spawn.SpawnCommand
import dev.jaims.moducore.bukkit.command.speed.FlySpeedCommand
import dev.jaims.moducore.bukkit.command.speed.SpeedCommand
import dev.jaims.moducore.bukkit.command.speed.WalkSpeedCommand
import dev.jaims.moducore.bukkit.command.teleport.*
import dev.jaims.moducore.bukkit.command.warp.DeleteWarpCommand
import dev.jaims.moducore.bukkit.command.warp.SetWarpCommand
import dev.jaims.moducore.bukkit.command.warp.WarpCommand
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.listener.*
import dev.jaims.moducore.bukkit.placeholder.ModuCorePlaceholderExpansion
import dev.jaims.moducore.bukkit.util.notifyVersion
import dev.jaims.moducore.bukkit.util.serverStartTime
import io.papermc.lib.PaperLib
import java.util.*
import java.util.logging.Level

class ModuCore : KotlinPlugin() {

    lateinit var api: DefaultModuCoreAPI

    // only null if they dont have the bot enabled in the modules
    val resourceId = 88602

    override fun enable() {
        // use paper lol
        PaperLib.suggestPaper(this, Level.WARNING)

        /*if (api.fileManager.modules[Modules.DISCORD_BOT]) {
            // TODO
        }*/

        notifyVersion(this)

        ModuCorePlaceholderExpansion(this).register()
        api.vaultEconomyProvider.register()

        serverStartTime = Date()
    }

    override fun disable() {
        // save player data
        api.storageManager.updateTask.cancel()
        api.storageManager.saveAllData(api.storageManager.playerDataCache)

        // unregister vault
        api.vaultEconomyProvider.unregister()

        // unregister the api
        api.unregisterServiceProvider()

        // holograms
        api.hologramManager.hololibManager.cachedHolograms.forEach { holo -> api.hologramManager.saveHologram(holo.name, holo) }
    }


    override fun registerCommands() {

        val modules = this.api.fileManager.modules

        // add a list of elements
        fun <T> MutableList<T>.addMultiple(vararg element: T): MutableList<T> {
            element.forEach {
                add(it)
            }
            return this
        }

        if (modules[Modules.ECONOMY]) allCommands.addMultiple(
            BalanceCommand(this),
            EconomyCommand(this),
            PayCommand(this)
        )
        if (modules[Modules.COMMAND_GAMEMODE]) allCommands.addMultiple(
            GamemodeAdventure(this),
            GamemodeCreative(this),
            GamemodeSpectator(this),
            GamemodeSurvival(this)
        )
        if (modules[Modules.COMMAND_GAMEMODE]) allCommands.addMultiple(
            SethomeCommand(this),
            DelhomeCommand(this),
            HomeCommand(this),
            HomesCommand(this)
        )
        if (modules[Modules.COMMAND_NICKNAME]) allCommands.addMultiple(
            NicknameCommand(this),
            NicknameRemoveCommand(this)
        )
        if (modules[Modules.COMMAND_REPAIR]) allCommands.addMultiple(
            Repair(this),
            RepairAll(this)
        )
        if (modules[Modules.SPAWN]) allCommands.addMultiple(
            SetSpawnCommand(this),
            SpawnCommand(this)
        )
        if (modules[Modules.COMMAND_SPEED]) allCommands.addMultiple(
            FlySpeedCommand(this),
            SpeedCommand(this),
            WalkSpeedCommand(this)
        )
        if (modules[Modules.COMMAND_TELEPORT]) allCommands.addMultiple(
            TeleportCommand(this),
            TeleportHereCommand(this),
            TeleportPlayerToPlayerCommand(this),
            TeleportPositionCommand(this)
        )
        if (modules[Modules.COMMAND_RANDOM_TELEPORT]) allCommands.add(RandomTeleportCommand(this))
        if (modules[Modules.COMMAND_WARPS]) allCommands.addMultiple(
            DeleteWarpCommand(this),
            SetWarpCommand(this),
            WarpCommand(this)
        )
        if (modules[Modules.COMMAND_CLEARINVENTORY]) allCommands.add(ClearInventoryCommand(this))
        if (modules[Modules.COMMAND_DISPOSE]) allCommands.add(DisposeCommand(this))
        if (modules[Modules.COMMAND_FEED]) allCommands.add(FeedCommand(this))
        if (modules[Modules.COMMAND_FLY]) allCommands.add(FlyCommand(this))
        if (modules[Modules.COMMAND_GIVE]) allCommands.add(GiveCommand(this))
        if (modules[Modules.COMMAND_HEAL]) allCommands.add(HealCommand(this))
        if (modules[Modules.HOLOGRAMS]) allCommands.add(HologramCommand(this))
        if (modules[Modules.COMMAND_PING]) allCommands.add(PingCommand(this))
        if (modules[Modules.COMMAND_HELP]) allCommands.add(HelpCommand(this))
        if (modules[Modules.COMMAND_TPS]) allCommands.add(TicksPerSecondCommand(this))
        if (modules[Modules.COMMAND_TIME]) allCommands.add(TimeCommand(this))
        if (modules[Modules.COMMAND_WEATHER]) allCommands.add(WeatherCommand(this))
        allCommands.add(SudoCommand(this))
        allCommands.add(ModuCoreReloadCommand(this))
        allCommands.add(ModuCoreDumpCommand(this))

        allCommands.forEach {
            it.register(this)
        }
    }

    override fun registerListeners() {
        register(
            SignChangeListener(this),
            PlayerChatListener(this),
            PlayerInteractListener(this),
            PlayerJoinListener(this),
            PlayerQuitListener(this)
        )

    }

    override fun registerManagers() {
        api = DefaultModuCoreAPI(this)
    }
}