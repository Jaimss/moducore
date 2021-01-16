package dev.jaims.moducore.bukkit.command.economy

import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.mcutils.common.InputType
import dev.jaims.mcutils.common.getInputType
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class BalanceCommand(override val plugin: ModuCore) : BaseCommand
{
    override val usage: String = "/balance [target]"
    override val description: String = "Check your or another players balance."
    override val commandName: String = "balance"

    private val economyManager = plugin.api.economyManager
    private val fileManager = plugin.api.fileManager
    private val playerManager = plugin.api.playerManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        when (args.size)
        {
            0 ->
            {
                if (!Perm.BALANCE.has(sender)) return
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return
                }
                val balance = economyManager.getBalance(sender.uniqueId)
                sender.send(fileManager.getString(Lang.BALANCE, sender).replace("{balance}", decimalFormat.format(balance)))
            }
            1 ->
            {
                if (!Perm.BALANCE_TARGET.has(sender)) return
                var target: Player? = null
                val uuid = if (args[0].getInputType() == InputType.NAME)
                {
                    target = playerManager.getTargetPlayer(args[0]) ?: run {
                        sender.playerNotFound(args[0])
                        return
                    }
                    target.uniqueId
                } else
                {
                    UUID.fromString(args[0])
                }
                val balance = economyManager.getBalance(uuid)
                sender.send(fileManager.getString(Lang.BALANCE, target).replace("{balance}", decimalFormat.format(balance)))
            }
            else -> sender.usage(usage, description)
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>
    {
        return mutableListOf<String>().apply {
            when (args.size)
            {
                1 -> addAll(playerManager.getPlayerCompletions(args[0]))
            }
        }

    }
}