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

package dev.jaims.moducore.api

import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import kotlin.system.measureTimeMillis

abstract class ModuCorePlugin : JavaPlugin() {
    abstract val api: ModuCoreAPI

    fun log(message: String) = logger.info(ChatColor.translateAlternateColorCodes('&', message))
    fun warn(message: String) = logger.warning(ChatColor.translateAlternateColorCodes('&', message))
    fun severe(message: String) = logger.severe(ChatColor.translateAlternateColorCodes('&', message))

    override fun onEnable() {
        log("&aEnabling ${description.name} (v${description.version})...")
        val millis = measureTimeMillis {

            // register managers first
            registerManagers()
            registerCommands()
            registerListeners()

            // call our enable code
            enable()
        }
        log("&a${description.name} (v${description.version}) enabled in &e${millis}&ams!")
    }

    /**
     * Code to run in the onEnable block.
     */
    abstract fun enable()

    override fun onDisable() {
        log("&cDisabling ${description.name} (v${description.version})...")
        val millis = measureTimeMillis {
            // call the disable method
            disable()
        }
        log("&c${description.name} (v${description.version}) disabled in &e${millis}&cms.")
    }

    /**
     * Disable logic.
     */
    abstract fun disable()

    /**
     * Code to register all managers/api things.
     */
    abstract fun registerManagers()

    /**
     * Code the register all the plugins listeners.
     */
    abstract fun registerListeners()

    /**
     * Code to register commands.
     */
    abstract fun registerCommands()
}