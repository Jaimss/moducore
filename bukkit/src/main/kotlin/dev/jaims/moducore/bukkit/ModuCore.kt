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

import dev.jaims.mcutils.bukkit.log
import dev.jaims.moducore.bukkit.api.DefaultModuCoreAPI
import dev.jaims.moducore.bukkit.external.ModuCorePlaceholderExpansion
import dev.jaims.moducore.bukkit.util.registerCommands
import dev.jaims.moducore.bukkit.util.registerEvents
import me.bristermitten.pdm.PluginDependencyManager
import org.bukkit.plugin.java.JavaPlugin
import kotlin.system.measureTimeMillis

class ModuCore : JavaPlugin() {

    lateinit var api: DefaultModuCoreAPI

    // plugin startup logic
    override fun onEnable() {
        val millis = measureTimeMillis {
            // load pdm dependencies
            PluginDependencyManager.of(this).loadAllDependencies().join()
            log("&aModucore is starting... (Version: ${description.version})")

            // get and check latest version
            // notifyVersion()

            // register all managers/commands/events/api stuff
            api = DefaultModuCoreAPI(this)
            registerCommands()
            registerEvents()

            ModuCorePlaceholderExpansion(this).register()
        }
        log("&aModuCore enabled in ${millis}ms! (Version: ${description.version})")
    }

    // plugin shutdown logic
    override fun onDisable() {
        val millis = measureTimeMillis {
            log("&cModuCore disabling... (Version: ${description.version})")

            // save player data
            api.storageManager.updateTask.cancel()
            api.storageManager.saveAllData(api.storageManager.playerData)
        }
        log("&cModuCore disabled in ${millis}ms. (Version: ${description.version})")
    }

}