package dev.jaims.jcore.bukkit.manager.config

import dev.jaims.jcore.bukkit.JCore
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class FileManager(plugin: JCore) {

    private val langFile = File(plugin.dataFolder, "lang.yml")
    lateinit var lang: YamlConfiguration

    /**
     * Create the different files when the filemanager is created
     */
    init {
        plugin.saveDefaultConfig()
        createLangFile()

        enumToFile(Config.values(), plugin.config, File(plugin.dataFolder, "config.yml"))
        enumToFile(Lang.values(), lang, langFile)
    }

    /**
     * Create the language file for the plugin
     */
    private fun createLangFile() {
        if (!langFile.exists()) langFile.createNewFile()
        lang = YamlConfiguration.loadConfiguration(langFile)
    }

    /**
     * Save stuff from an enum to the language file if it doesn't exist.
     */
    private fun <T : ConfigFileEnum> enumToFile(enumValues: Array<T>, file: FileConfiguration, filePath: File) {
        for (e in enumValues) {
            val current = file.get(e.path)
            if (current == null) {
                file.set(e.path, e.default)
                file.save(filePath)
            }
        }
    }
}

/**
 * A config file enum interface that provides a path and a default value of any type.
 */
interface ConfigFileEnum {
    val path: String
    val default: Any
}