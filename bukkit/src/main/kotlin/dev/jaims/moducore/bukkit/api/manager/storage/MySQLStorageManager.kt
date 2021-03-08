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

package dev.jaims.moducore.bukkit.api.manager.storage

import com.github.shynixn.mccoroutine.launchAsync
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.jaims.moducore.api.data.LocationHolder
import dev.jaims.moducore.api.data.PlayerData
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import kotlinx.coroutines.*
import java.util.*

class MySQLStorageManager(val plugin: ModuCore) : StorageManager() {
    override val updateTask: Job = plugin.launchAsync {
        saveAllData(playerDataCache)
        while (true) {
            delay(60 * 1000)
        }
    }

    override val playerDataCache: MutableMap<UUID, PlayerData> = mutableMapOf()

    private val hikariDataSource: HikariDataSource

    init {
        hikariDataSource = HikariDataSource(getHikariConfig())
        createTables()
    }

    /**
     * Create the hikari config.
     */
    private fun getHikariConfig(): HikariConfig {
        val config = plugin.api.fileManager.config
        val address = config[Config.MYSQL_ADDRESS]
        val port = config[Config.MYSQL_PORT]
        val username = config[Config.MYSQL_USERNAME]
        val password = config[Config.MYSQL_PASS]
        val ssl = config[Config.MYSQL_USE_SSL]

        val hikariConfig = HikariConfig()
        with(hikariConfig) {
            jdbcUrl = "jdbc:mysql://$address:$port?autoReconnect=true&useSSL=$ssl&allowPublicKeyRetrieval=true"
            this.username = username
            this.password = password
            this.minimumIdle = 1
            this.maximumPoolSize = 25
            this.connectionTimeout = 1000
            this.connectionTestQuery = "SELECT 1;"
        }
        return hikariConfig
    }

    /**
     * Create the mysql tables that need to exist.
     */
    private fun createTables() {
        // create the database
        hikariDataSource.use { dataSource ->
            val preparedStatement =
                dataSource.connection.prepareStatement("CREATE DATABASE IF NOT EXISTS ${plugin.api.fileManager.config[Config.MYSQL_DATABASE]};")
            preparedStatement.executeUpdate()
        }
        // create the table
        hikariDataSource.use { dataSource ->
            val preparedStatement = dataSource.connection.prepareStatement(
                """CREATE TABLE `moducore`.`player_data` (
                    `uuid` LONGTEXT NOT NULL,
                    `nickname` VARCHAR(45) NULL,
                    `balance` DOUBLE ZEROFILL NOT NULL,
                    `homes` JSON NULL,
                    PRIMARY KEY (`uuid`),
                    UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC));
                """.trimIndent()
            )
            preparedStatement.executeUpdate()
        }
    }

    /**
     * Get all the player data in the storage folder.
     *
     * @return a list of [PlayerData]
     */
    override suspend fun getAllData(): List<PlayerData> {
        val dataList = mutableListOf<PlayerData>()
        val query = "SELECT * FROM `moducore`.`player_data`;"
        hikariDataSource.use { dataSource ->
            val statment = dataSource.connection.prepareStatement(query)
            val rs = statment.executeQuery()
            while (rs.next()) {
                val uuid = UUID.fromString(rs.getString("uuid"))
                dataList.add(getPlayerData(uuid))
            }
        }
        return dataList
    }

    /**
     * Gets the [PlayerData] for a player. PlayerData is stored in a file.
     *
     * @param uuid the uuid of the player.
     *
     * @return the [PlayerData]
     */
    override suspend fun getPlayerData(uuid: UUID): PlayerData {
        // cached
        val cachedData = playerDataCache[uuid]
        if (cachedData != null) return cachedData
        // get from database if not cached
        val query = "SELECT * FROM `moducore`.`player_data` WHERE `uuid`=?;"
        val deferredData = GlobalScope.async(Dispatchers.IO) {
            hikariDataSource.use { dataSource ->
                val preparedStatement = dataSource.connection.prepareStatement(query)
                preparedStatement.setString(0, uuid.toString())
                val rs = preparedStatement.executeQuery()
                while (rs.next()) {
                    val nickName = rs.getString("nickname") ?: null
                    val balance = rs.getDouble("balance")
                    val homesRaw = rs.getString("homes")
                    val homes =
                        if (homesRaw != null) gson.fromJson(homesRaw, mutableMapOf<String, LocationHolder>()::class.java) else mutableMapOf()
                    return@async PlayerData(nickName, balance, homes)
                }
            }
            return@async null
        }
        // get the data
        val data = deferredData.await()!!
        setPlayerData(uuid, data)
        return data
    }

    /**
     * Save all the player data cache back to the storage.
     *
     * @param allData the data to save
     */
    override suspend fun saveAllData(allData: Map<UUID, PlayerData>) {
        TODO("Not yet implemented")
    }

    /**
     * Set the [PlayerData] for a player.
     *
     * @param uuid the uuid of the player
     * @param playerData the relevant playerdata
     */
    override suspend fun setPlayerData(uuid: UUID, playerData: PlayerData) {
        TODO("Not yet implemented")
    }
}