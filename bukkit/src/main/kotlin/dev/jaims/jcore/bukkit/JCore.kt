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

import dev.jaims.jcore.bukkit.api.JCoreAPIImpl
import dev.jaims.jcore.bukkit.external.JCorePAPIExpansion
import dev.jaims.jcore.bukkit.util.getLatestVersion
import dev.jaims.jcore.bukkit.util.registerCommands
import dev.jaims.jcore.bukkit.util.registerEvents
import dev.jaims.mcutils.bukkit.log
import me.bristermitten.pdm.PluginDependencyManager
import org.bukkit.plugin.java.JavaPlugin
import javax.print.attribute.standard.Severity

class JCore : JavaPlugin() {

    lateinit var api: JCoreAPIImpl

    // plugin startup logic
    override fun onEnable() {
        PluginDependencyManager.of(this).loadAllDependencies().join()
        log("&aJCore is starting... &2(Version: ${description.version})")

        // get and check latest version
        val latestVersion = getLatestVersion(86911)
        if (latestVersion != null && latestVersion != description.version) {
            log(
                "There is a new version of JCore Available ($latestVersion)! Please download it from https://www.spigotmc.org/resources/86911/",
                Severity.WARNING
            )
        }

        // register all managers/commands/events
        api = JCoreAPIImpl(this)
        registerCommands()
        registerEvents()

        JCorePAPIExpansion(this).register()

        log("&aJCore enabled! &2(Version: ${description.version})")
    }

    // plugin shutdown logic
    override fun onDisable() {
        log("&cJCore disabling... (Version: ${description.version})")

        api.unregisterVault()

        log("&cJCore disabled. (Version: ${description.version})")
    }

}