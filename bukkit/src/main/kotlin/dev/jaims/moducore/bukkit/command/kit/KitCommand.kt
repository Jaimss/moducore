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

package dev.jaims.moducore.bukkit.command.kit

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.api.data.Kit
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.*
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class KitCommand(override val plugin: ModuCore) : BaseCommand {

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        val kitName = args.firstOrNull()?.toLowerCase() ?: run {
            sender.usage(usage, description)
            val names = kitManager.kitCache.map { it.name }
            sender.send(Lang.KITS_AVAILABLE) { it.replace("{kits}", if (names.isEmpty()) "None" else names.joinToString(", ")) }
            return
        }
        val kit = kitManager.getKit(kitName) ?: run {
            sender.send(Lang.KIT_NOT_FOUND) { it.replace("{name}", kitName) }
            return
        }

        // regular perms
        if (!Permissions.USE_KIT.has(sender) { it.replace("<kitname>", kitName) }) return
        val targetName = args.getOrNull(1)
        if (targetName != null) {
            // other perms
            if (!Permissions.USE_KIT_OTHERS.has(sender) { it.replace("<kitname>", kitName) }) return
            val target = playerManager.getTargetPlayer(targetName) ?: run {
                sender.playerNotFound(targetName)
                return
            }
            kit.give(target)
            target.send(Lang.KIT_CLAIMED, sender) { it.replace("{name}", kitName) }
            sender.send(Lang.KIT_CLAIMED_TARGET, target) { it.replace("{name}", kitName) }
            return
        }

        // personal kit
        // coldown checking
        if (!Permissions.USE_KIT_BYPASS_COOLDOWN.has(sender, false) { it.replace("<kitname>", kitName) }) {
            val timeClaimed = storageManager.getPlayerData(sender.uniqueId).kitClaimTimes[kitName]
            println("timeClaimed = ${timeClaimed}")
            println("System.currentTimeMillis() = ${System.currentTimeMillis()}")
            if (timeClaimed != null) {
                val timeSinceClaim = (System.currentTimeMillis() - timeClaimed) / 1000
                println("timeSinceClaim = ${timeSinceClaim}s")
                println("kit.cooldown = ${kit.cooldown}s")
                if (timeSinceClaim <= kit.cooldown) {
                    sender.send(Lang.KIT_COOLDOWN) {
                        it.replace("{name}", kitName)
                            .replace("{time}", (kit.cooldown - timeSinceClaim).toInt().cooldownFormat)
                    }
                    return
                }
            }
        }
        kit.give(sender)
        // set the data
        val playerData = storageManager.getPlayerData(sender.uniqueId)
        playerData.kitClaimTimes[kit.name] = System.currentTimeMillis()
        // success
        sender.send(Lang.KIT_CLAIMED) { it.replace("{name}", kit.name) }
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(kitManager.kitCache.filter { it.name.startsWith(args[0]) }.map { it.name })
            }
        }
    }

    override val module: Property<Boolean> = Modules.KITS
    override val usage: String = "/kit <name> [target]"
    override val description: String = "Give a kit to a player."
    override val commandName: String = "kit"
    override val brigadierSyntax: LiteralArgumentBuilder<*>? = LiteralArgumentBuilder.literal<String>(commandName)
        .then(RequiredArgumentBuilder.argument<String, String>("name", StringArgumentType.word())
            .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word())))

}