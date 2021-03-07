package dev.jaims.moducore.bukkit.command.hologram

import dev.jaims.hololib.core.Hologram
import dev.jaims.hololib.core.HologramPage
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.send
import org.bukkit.entity.Player

/**
 * @return the hologram that was searched for with the command
 */
fun getHologram(sender: Player, name: String, command: BaseCommand): Hologram? {
    return command.hologramManager.getFromCache(name) ?: run {
        sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
        return null
    }
}

/**
 * @return the page index or null
 */
fun getPageIndex(name: String, sender: Player, hologram: Hologram): Int? {
    return hologram.getCurrentPageIndex(sender) ?: run {
        sender.send(Lang.HOLO_NOT_VIEWING_PAGE, sender) { it.replace("{name}", name) }
        return null
    }
}

/**
 * @return true if the page index is a valid index
 */
fun validatePageIndex(name: String, sender: Player, hologram: Hologram, pageIndex: Int): Boolean {
    if (pageIndex >= hologram.pages.size || pageIndex < 0) {
        sender.send(Lang.INDEX_OUT_OF_BOUNDS, sender) { it.replace("{name}", name) }
        return false
    }
    return true
}

/**
 * @return true if the line index is valid for this hologram
 */
fun validateLineIndex(name: String, sender: Player, page: HologramPage, lineIndex: Int): Boolean {
    if (lineIndex >= page.lines.size || lineIndex < 0) {
        sender.send(Lang.INDEX_OUT_OF_BOUNDS, sender) { it.replace("{name}", name) }
        return false
    }
    return true
}