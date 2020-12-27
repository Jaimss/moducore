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

package dev.jaims.jcore.bukkit.util

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.command.*
import dev.jaims.jcore.bukkit.command.gamemode.GamemodeAdventure
import dev.jaims.jcore.bukkit.command.gamemode.GamemodeCreative
import dev.jaims.jcore.bukkit.command.gamemode.GamemodeSpectator
import dev.jaims.jcore.bukkit.command.gamemode.GamemodeSurvival
import dev.jaims.jcore.bukkit.command.repair.Repair
import dev.jaims.jcore.bukkit.command.repair.RepairAll
import dev.jaims.jcore.bukkit.event.listener.PlayerChatListener
import dev.jaims.jcore.bukkit.event.listener.PlayerInteractListener
import dev.jaims.jcore.bukkit.event.listener.PlayerJoinListener
import dev.jaims.jcore.bukkit.event.listener.PlayerQuitListener
import dev.jaims.jcore.bukkit.config.Modules
import dev.jaims.mcutils.bukkit.register

/**
 * Method to register the events of [JCore]
 */
internal fun JCore.registerEvents() {
    this.register(
        PlayerChatListener(this),
        PlayerInteractListener(this),
        PlayerJoinListener(this),
        PlayerQuitListener(this)
    )
}

/**
 * Method to register the commands of [JCore]
 */
internal fun JCore.registerCommands() {
    val modules = this.api.fileManager.modules

    // add a list of elements
    fun <T> MutableList<T>.addMultiple(vararg element: T): MutableList<T> {
        element.forEach {
            add(it)
        }
        return this
    }

    if (modules.getProperty(Modules.COMMAND_GAMEMODE)) allCommands.addMultiple(
        GamemodeAdventure(this),
        GamemodeCreative(this),
        GamemodeSpectator(this),
        GamemodeSurvival(this)
    )
    if (modules.getProperty(Modules.COMMAND_REPAIR)) allCommands.addMultiple(
        Repair(this),
        RepairAll(this)
    )
    if (modules.getProperty(Modules.COMMAND_CLEARINVENTORY)) allCommands.add(ClearInventoryCommand(this))
    if (modules.getProperty(Modules.COMMAND_DISPOSE)) allCommands.add(DisposeCommand(this))
    if (modules.getProperty(Modules.COMMAND_FEED)) allCommands.add(FeedCommand(this))
    if (modules.getProperty(Modules.COMMAND_FLY)) allCommands.add(FlyCommand(this))
    if (modules.getProperty(Modules.COMMAND_GIVE)) allCommands.add(GiveCommand(this))
    if (modules.getProperty(Modules.COMMAND_HEAL)) allCommands.add(HealCommand(this))
    if (modules.getProperty(Modules.COMMAND_HELP)) allCommands.add(HelpCommand(this))
    allCommands.add(ReloadCommand(this))

    allCommands.forEach {
        it.register(this)
    }
}