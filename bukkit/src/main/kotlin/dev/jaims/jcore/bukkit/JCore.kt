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

import dev.jaims.jcore.bukkit.manager.Managers
import dev.jaims.jcore.bukkit.manager.pdmDependencySetup
import dev.jaims.jcore.bukkit.manager.registerCommands
import dev.jaims.jcore.bukkit.manager.registerEvents
import dev.jaims.mcutils.bukkit.log
import org.bukkit.plugin.java.JavaPlugin

class JCore : JavaPlugin() {

    // plugin startup logic
    override fun onEnable() {
        pdmDependencySetup(this)
        log("&aJCore is starting... &2(Version: ${description.version})")

        // register all managers/commands/events
        managers = Managers(this)
        registerCommands(this)
        registerEvents(this)

        log("&aJCore enabled! &2(Version: ${description.version})")
    }

    lateinit var managers: Managers

    // plugin shutdown logic
    override fun onDisable() {
        log("&cJCore disabling... (Version: ${description.version})")

        log("&cJCore disabled. (Version: ${description.version})")
    }

}