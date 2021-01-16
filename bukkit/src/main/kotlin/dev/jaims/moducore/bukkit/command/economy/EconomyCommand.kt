package dev.jaims.moducore.bukkit.command.economy

import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class EconomyCommand(override val plugin: ModuCore) : BaseCommand
{
    override val usage: String = "/eco <set|give|take> <amount> <target>"
    override val description: String = "Manage the server's economy."
    override val commandName: String = "economy"

    private val playerManager = plugin.api.playerManager
    private val fileManager = plugin.api.fileManager
    private val economyManager = plugin.api.economyManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        if (args.size != 3)
        {
            sender.usage(usage, description)
            return
        }
        if (!Perm.ECONOMY.has(sender)) return

        val target = playerManager.getTargetPlayer(args[2]) ?: run {
            sender.playerNotFound(args[2])
            return
        }

        val amount = args[1].toDoubleOrNull() ?: run {
            sender.invalidNumber()
            return
        }
        if (amount < 0)
        {
            sender.invalidNumber()
            return
        }

        when (args[0])
        {
            "set" ->
            {
                economyManager.setBalance(target.uniqueId, amount)
                if (!props.isSilent)
                {
                    target.send(fileManager.getString(Lang.ECONOMY_SET_TARGET).replace("{amount}", decimalFormat.format(amount)))
                }
                sender.send(fileManager.getString(Lang.ECONOMY_SET, target).replace("{amount}", decimalFormat.format(amount)))
            }
            "give" ->
            {
                economyManager.deposit(target.uniqueId, amount)
                if (!props.isSilent)
                {
                    target.send(fileManager.getString(Lang.ECONOMY_GIVE_TARGET).replace("{amount}", decimalFormat.format(amount)))
                }
                sender.send(fileManager.getString(Lang.ECONOMY_GIVE, target).replace("{amount}", decimalFormat.format(amount)))
            }
            "take" ->
            {
                if (!economyManager.hasSufficientFunds(target.uniqueId, amount))
                {
                    sender.send(fileManager.getString(Lang.INSUFFICIENT_FUNDS, target))
                    return
                }
                economyManager.withdraw(target.uniqueId, amount)
                if (!props.isSilent)
                {
                    target.send(fileManager.getString(Lang.ECONOMY_TAKE_TARGET).replace("{amount}", decimalFormat.format(amount)))
                }
                sender.send(fileManager.getString(Lang.ECONOMY_TAKE, target).replace("{amount}", decimalFormat.format(amount)))
            }
            else -> sender.usage(usage, description)
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>
    {
        return mutableListOf<String>().apply {
            when (args.size)
            {
                1 -> addAll(listOf("set", "give", "take").filter { it.startsWith(args[0], ignoreCase = true) })
                3 -> addAll(playerManager.getPlayerCompletions(args[2]))
            }
        }
    }
}