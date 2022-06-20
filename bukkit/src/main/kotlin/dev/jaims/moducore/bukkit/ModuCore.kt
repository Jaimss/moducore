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

import com.github.shynixn.mccoroutine.registerSuspendingEvents
import dev.jaims.mcutils.bukkit.KotlinPlugin
import dev.jaims.moducore.bukkit.api.DefaultModuCoreAPI
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.allCommands
import dev.jaims.moducore.bukkit.func.notifyVersion
import dev.jaims.moducore.bukkit.func.serverStartTime
import dev.jaims.moducore.bukkit.func.tps
import dev.jaims.moducore.bukkit.listener.*
import dev.jaims.moducore.bukkit.metrics.moduleMetric
import dev.jaims.moducore.bukkit.placeholder.ModuCorePlaceholderExpansion
import dev.jaims.moducore.bukkit.tasks.startBroadcast
import dev.jaims.moducore.libs.org.bstats.bukkit.Metrics
import io.papermc.lib.PaperLib
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.event.Listener
import org.reflections.Reflections
import java.util.*
import java.util.logging.Level

/**
 * The main class for the ModuCore bukkit plugin
 */
class ModuCore : KotlinPlugin() {

    lateinit var api: DefaultModuCoreAPI
    lateinit var audience: BukkitAudiences

    private val bStatsId = 11030
    val resourceId = 88602

    override fun enable() {
        // use paper lol
        PaperLib.suggestPaper(this, Level.WARNING)

        // bstats
        Metrics(this, bStatsId)
            .moduleMetric(this)

        /*if (api.fileManager.modules[Modules.DISCORD_BOT]) {
            // TODO
        }*/

        // start tps
        server.scheduler.scheduleSyncRepeatingTask(this, tps, 100, 1)

        notifyVersion(this)

        audience = BukkitAudiences.create(this)

        startBroadcast(this)

        ModuCorePlaceholderExpansion(this).register()

        api.vaultEconomyProvider.register()

        serverStartTime = Date()
    }

    override fun disable() {
        // save player data
        api.storageManager.updateTask.cancel()
        runBlocking { api.storageManager.saveAllData(api.storageManager.playerDataCache) }

        // unregister vault
        api.vaultEconomyProvider.unregister()

        // unregister the api
        api.unregisterServiceProvider()

        // holograms
        api.hologramManager.hololibManager.cachedHolograms.forEach { holo ->
            api.hologramManager.saveHologram(holo.name, holo)
        }
    }

    override fun registerCommands() {
        val modules = this.api.fileManager.modules
        Reflections("dev.jaims.moducore.bukkit.command")
            .getSubTypesOf(BaseCommand::class.java)
            .forEach {
                val command = it.getConstructor(ModuCore::class.java).newInstance(this)
                // make sure module is enabled
                if (command.module == null) {
                    allCommands.add(command)
                    command.register(this)
                }
                if (command.module != null && modules[command.module!!]) {
                    // add the command
                    allCommands.add(command)
                    command.register(this)
                }
            }
    }

    override fun registerListeners() {
        registerSuspendingListener(
            SignChangeListener(this),
            PlayerChatListener(this),
            PlayerInteractListener(this),
            PlayerJoinListener(this),
            PlayerQuitListener(this),
            PlayerDeathListener(this)
        )

    }

    override fun registerManagers() {
        api = DefaultModuCoreAPI(this)
    }

    private fun registerSuspendingListener(vararg listeners: Listener) {
        listeners.forEach {
            server.pluginManager.registerSuspendingEvents(it, this)
        }
    }
}