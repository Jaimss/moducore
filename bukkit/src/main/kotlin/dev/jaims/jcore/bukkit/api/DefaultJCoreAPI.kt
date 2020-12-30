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

package dev.jaims.jcore.bukkit.api

import dev.jaims.jcore.api.JCoreAPI
import dev.jaims.jcore.api.JCoreAPI.Companion.instance
import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.api.manager.DefaultPlayerManager
import dev.jaims.jcore.bukkit.api.manager.DefaultPlaytimeManager
import dev.jaims.jcore.bukkit.api.manager.DefaultStorageManager
import dev.jaims.jcore.bukkit.config.FileManager
import dev.jaims.jcore.bukkit.vault.JCoreEconomy
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.ServicePriority

class DefaultJCoreAPI(private val plugin: JCore) : JCoreAPI
{

    // internal
    val fileManager: FileManager
    lateinit var economy: JCoreEconomy

    // api
    override val playerManager: DefaultPlayerManager
    override val playtimeManager: DefaultPlaytimeManager
    override val storageManager: DefaultStorageManager

    init
    {
        instance = this

        setupVault()

        fileManager = FileManager(plugin)
        storageManager = DefaultStorageManager(plugin)
        playerManager = DefaultPlayerManager(plugin)
        playtimeManager = DefaultPlaytimeManager(plugin)
    }

    // instance of the economy & register it
    private fun setupVault()
    {
        economy = JCoreEconomy(plugin)
        plugin.server.servicesManager.register(Economy::class.java, economy, plugin, ServicePriority.Highest)
    }

    fun unregisterVault()
    {
        plugin.server.servicesManager.unregister(Economy::class.java, economy)
    }

}