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

package dev.jaims.jcore.bukkit

import dev.jaims.jcore.bukkit.api.DefaultJCoreAPI
import dev.jaims.jcore.bukkit.external.JCorePAPIExpansion
import dev.jaims.jcore.bukkit.util.notifyVersion
import dev.jaims.jcore.bukkit.util.registerCommands
import dev.jaims.jcore.bukkit.util.registerEvents
import dev.jaims.mcutils.bukkit.log
import me.bristermitten.pdm.PluginDependencyManager
import org.bukkit.plugin.java.JavaPlugin
import kotlin.system.measureTimeMillis

class JCore : JavaPlugin()
{

    lateinit var api: DefaultJCoreAPI

    // plugin startup logic
    override fun onEnable()
    {
        val millis = measureTimeMillis {
            // load pdm dependencies
            PluginDependencyManager.of(this).loadAllDependencies().join()
            log("&aJCore is starting... (Version: ${description.version})")

            // get and check latest version
            notifyVersion()

            // register all managers/commands/events/api stuff
            api = DefaultJCoreAPI(this)
            registerCommands()
            registerEvents()

            JCorePAPIExpansion(this).register()
        }
        log("&aJCore enabled in ${millis}ms! (Version: ${description.version})")
    }

    // plugin shutdown logic
    override fun onDisable()
    {
        val millis = measureTimeMillis {
            log("&cJCore disabling... (Version: ${description.version})")

            // save player data
            api.storageManager.updateTask.cancel()
            api.storageManager.saveAllData(api.storageManager.playerData)
        }
        log("&cJCore disabled in ${millis}ms. (Version: ${description.version})")
    }

}