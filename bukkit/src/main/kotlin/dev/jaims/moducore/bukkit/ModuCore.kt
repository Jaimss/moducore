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
import org.reflections.Reflections
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
        Reflections("dev.jaims.moducore.bukkit.command")
            .getSubTypesOf(BaseCommand::class.java)
            .forEach {
                val command = it.getConstructor(ModuCore::class.java).newInstance(this)
                // make sure module is enabled
                if (command.module != null && !modules[command.module!!]) return@forEach
                // add the command
                allCommands.add(command)
                command.register(this)
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