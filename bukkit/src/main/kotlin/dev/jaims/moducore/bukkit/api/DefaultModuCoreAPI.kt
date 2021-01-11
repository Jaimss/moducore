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

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.api.ModuCoreAPI.Companion.instance
import dev.jaims.moducore.api.manager.EconomyManager
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.api.manager.DefaultEconomyManager
import dev.jaims.moducore.bukkit.api.manager.DefaultPlayerManager
import dev.jaims.moducore.bukkit.api.manager.DefaultPlaytimeManager
import dev.jaims.moducore.bukkit.api.manager.DefaultStorageManager
import dev.jaims.moducore.bukkit.config.FileManager

class DefaultModuCoreAPI(private val plugin: ModuCore) : ModuCoreAPI
{

    // internal
    val fileManager: FileManager

    // api
    override val playerManager: DefaultPlayerManager
    override val playtimeManager: DefaultPlaytimeManager
    override val storageManager: DefaultStorageManager
    override val economyManager: EconomyManager

    init
    {
        instance = this


        fileManager = FileManager(plugin)
        storageManager = DefaultStorageManager(plugin)
        playerManager = DefaultPlayerManager(plugin)
        playtimeManager = DefaultPlaytimeManager(plugin)
        economyManager = DefaultEconomyManager(plugin)
    }

}