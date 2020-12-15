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

package dev.jaims.jcore.bukkit.manager

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.command.HelpCommand
import dev.jaims.jcore.bukkit.command.allCommands
import dev.jaims.jcore.bukkit.command.fly.FlyCommand
import dev.jaims.jcore.bukkit.event.listener.PlayerJoinListener
import dev.jaims.jcore.bukkit.event.listener.PlayerQuitListener
import dev.jaims.jcore.bukkit.manager.config.FileManager
import dev.jaims.mcutils.bukkit.register
import me.bristermitten.pdm.PluginDependencyManager

/**
 * Managers class to avoid clutter in the main class
 */
class Managers(plugin: JCore) {
    val fileManager = FileManager(plugin)
    val playerManager = PlayerManager(plugin)
}

/**
 * Method to register the events of the plugin
 */
internal fun registerEvents(plugin: JCore) {
    plugin.register(
        PlayerJoinListener(plugin),
        PlayerQuitListener(plugin)
    )
}

/**
 * Method to register the commands.
 */
internal fun registerCommands(plugin: JCore) {
    allCommands.add(HelpCommand(plugin))
    allCommands.add(FlyCommand(plugin))

    allCommands.forEach {
        it.register(plugin)
    }
}

/**
 * Download dependencies using PDM to decrease Jar Size!
 */
internal fun pdmDependencySetup(plugin: JCore) {
    val pdm = PluginDependencyManager.of(plugin)
    pdm.loadAllDependencies().join()
}