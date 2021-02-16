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

package dev.jaims.moducore.bukkit.api

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.api.ModuCoreAPI.Companion.instance
import dev.jaims.moducore.api.manager.*
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.api.manager.*
import dev.jaims.moducore.bukkit.api.manager.storage.FileStorageManager
import dev.jaims.moducore.bukkit.config.FileManager
import dev.jaims.moducore.bukkit.vault.VaultEconomyProvider
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority

class DefaultModuCoreAPI(private val plugin: ModuCore) : ModuCoreAPI {

    // internal
    val fileManager: FileManager
    val vaultEconomyProvider: VaultEconomyProvider
    val protocolManager: ProtocolManager

    // api
    override val playerManager: PlayerManager
    override val playtimeManager: PlaytimeManager
    override val storageManager: StorageManager
    override val economyManager: EconomyManager
    override val locationManager: LocationManager
    override val hologramManager: HologramManager

    init {
        instance = this

        registerServiceProvider()

        fileManager = FileManager(plugin)
        protocolManager = ProtocolLibrary.getProtocolManager()

        storageManager = FileStorageManager(plugin)
        playerManager = DefaultPlayerManager(plugin)
        playtimeManager = DefaultPlaytimeManager(plugin)
        economyManager = DefaultEconomyManager(plugin)
        locationManager = DefaultLocationManager(plugin)
        hologramManager = DefaultHologramManager(plugin)

        vaultEconomyProvider = VaultEconomyProvider(plugin)
    }

    private fun registerServiceProvider() =
        Bukkit.getServicesManager().register(ModuCoreAPI::class.java, this, plugin, ServicePriority.Highest)

    fun unregisterServiceProvider() =
        Bukkit.getServicesManager().unregister(this)

}