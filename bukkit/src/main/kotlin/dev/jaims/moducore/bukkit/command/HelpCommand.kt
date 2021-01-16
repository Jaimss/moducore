package dev.jaims.moducore.bukkit.command

import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HelpCommand(override val plugin: ModuCore) : BaseCommand
{

    override val usage: String = "/help [command] [-p <page>]"
    override val description: String = "Show help menus for all commands or a specific one. You can set a page using -p number."
    override val commandName: String = "help"

    private val fileManager = plugin.api.fileManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        // get a list of commands to include
        var filter = args.getOrNull(0) ?: ""
        if (filter == "-p") filter = ""
        val matches = allCommands.filter { it.commandName.contains(filter, ignoreCase = true) }

        if (matches.isEmpty())
        {
            sender.send(fileManager.getString(Lang.HELP_NOT_FOUND).replace("{name}", filter))
            return
        }

        if (sender !is Player)
        {
            sender.send(
                mutableListOf<String>().apply {
                    matches.forEach {
                        add(fileManager.getString(Lang.HELP_COMMAND_USAGE, sender as? Player).replace("{usage}", it.usage))
                        add(fileManager.getString(Lang.HELP_COMMAND_DESCRIPTION, sender as? Player).replace("{description}", it.description))
                    }
                }
            )
            return
        }

        val chunked = matches.chunked(7)
        val pages = chunked.mapIndexed { index, commands ->
            // get the page or a default
            // TODO replace with buildList when its no longer experimental
            val lines = mutableListOf<String>().apply {
                commands.forEach {
                    add(fileManager.getString(Lang.HELP_COMMAND_USAGE, sender as? Player).replace("{usage}", it.usage))
                    add(fileManager.getString(Lang.HELP_COMMAND_DESCRIPTION, sender as? Player).replace("{description}", it.description))
                }
            }.toList()
            // add the new page
            Page(lines, index + 1, chunked.size, filter)
        }

        val pageIndex = args.indexOf("-p") + 1
        val page = args.getOrNull(pageIndex)?.toIntOrNull() ?: 1

        if (page <= 0 || page > pages.size)
        {
            sender.send(fileManager.getString(Lang.HELP_INVALID_PAGE))
            return
        }

        pages[page - 1].send(sender)
    }

    fun Page.send(sender: CommandSender)
    {

        sender.send(
            fileManager.getString(Lang.HELP_HEADER, sender as? Player)
                .replace("{filter}", if (filter == "") "none" else filter)
        )
        sender.send(lines.toList())
        sender.sendMessage(*ComponentBuilder().apply {
            // back
            append("««")
            bold(true)
            if (current >= 2) color(ChatColor.AQUA) else color(ChatColor.GRAY)
            if (current >= 2) event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help $filter -p ${current - 1}"))

            // page number
            append(" $current / $total ")
            color(ChatColor.GOLD)

            // forward
            append("»»")
            bold(true)
            if (current <= (total - 1)) color(ChatColor.AQUA) else color(ChatColor.GRAY)
            if (current <= (total - 1)) event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help $filter -p ${current + 1}"))
        }.create())
    }
}

data class Page(
    val lines: List<String>,
    val current: Int,
    val total: Int,
    val filter: String
)