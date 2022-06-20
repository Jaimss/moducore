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
import dev.jaims.moducore.bukkit.config.FileManager
import kotlinx.coroutines.*
import java.sql.SQLException
import java.util.*

class MySQLStorageManager(plugin: ModuCore, val fileManager: FileManager) : StorageManager() {
    override val updateTask: Job = plugin.launchAsync {
        saveAllData(playerDataCache)
        while (true) {
            delay((60 * 1000).toLong())
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
        val config = fileManager.config
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

    private fun addColumnIfNotExists(columnName: String, columnInfo: String) =
        """
            IF NOT EXISTS (SELECT NULL 
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE table_name = 'player_data'
                AND table_schema = 'moducore'
                AND column_name = '$columnName') THEN
                
            ALTER TABLE `moducore`.`player_data` ADD `$columnName` $columnInfo;
            END IF;
        """.trimIndent()

    /**
     * Create the mysql tables that need to exist.
     */
    private fun createTables() {
        // create the database
        hikariDataSource.connection.use { con ->
            val preparedStatement =
                con.prepareStatement("CREATE DATABASE IF NOT EXISTS ${fileManager.config[Config.MYSQL_DATABASE]};")
            preparedStatement.executeUpdate()
        }
        // create the table
        hikariDataSource.connection.use { con ->
            val preparedStatement = con.prepareStatement(
                """CREATE TABLE IF NOT EXISTS `moducore`.`player_data` (
                    `uuid` VARCHAR(36) NOT NULL,
                    `nickname` LONGTEXT NULL,
                    `balance` DOUBLE ZEROFILL NOT NULL,
                    `chatcolor` LONGTEXT NULL,
                    `chatpingsenabld` BOOLEAN NOT NULL,
                    `homes` JSON NULL,
                    `kit_claim_times` JSON NULL,
                    PRIMARY KEY (`uuid`),
                    UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC));
                """.trimIndent()
            )
            preparedStatement.executeUpdate()
            val addChatPings = con.prepareStatement(
                addColumnIfNotExists("chatpingsenabled", "BOOLEAN NOT NULL default TRUE")
            )
            addChatPings.executeUpdate()
            val addDiscordId = con.prepareStatement(
                addColumnIfNotExists("discordid", "BIGINT NULL default NULL")
            )
            addDiscordId.executeUpdate()
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
        hikariDataSource.connection.use { con ->
            val statment = con.prepareStatement(query)
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
            hikariDataSource.connection.use { con ->
                val preparedStatement = con.prepareStatement(query)
                preparedStatement.setString(1, uuid.toString())
                val rs = preparedStatement.executeQuery()
                while (rs.next()) {
                    // data
                    val nickName = rs.getString("nickname") ?: null
                    val balance = rs.getDouble("balance")
                    val chatcolor = rs.getString("chatcolor")
                    val chatPingsEnabled = try {
                        rs.getBoolean("chatpingsenabled")
                    } catch (ignored: SQLException) {
                        true
                    }
                    val discordID = try {
                        val id = rs.getLong("discordid")
                        if (id == 0L) null
                        else id
                    } catch (ignored: SQLException) {
                        null
                    }
                    // homes
                    val homesRaw = rs.getString("homes")
                    val homes = if (homesRaw != null) gson.fromJson(
                        homesRaw,
                        mutableMapOf<String, LocationHolder>()::class.java
                    ) else mutableMapOf()
                    // kits
                    val kitClaimTimesRaw = rs.getString("kit_claim_times")
                    val kitClaimTimes = if (kitClaimTimesRaw != null) gson.fromJson(
                        kitClaimTimesRaw,
                        mutableMapOf<String, Long>()::class.java
                    ) else mutableMapOf()
                    return@async PlayerData(
                        nickName,
                        balance,
                        chatcolor,
                        chatPingsEnabled,
                        discordID,
                        homes,
                        kitClaimTimes
                    )
                }
            }
            return@async null
        }
        // get the data
        val data = deferredData.await() ?: PlayerData()
        setPlayerData(uuid, data)
        return data
    }

    /**
     * Save all the player data cache back to the storage.
     *
     * @param allData the data to save
     */
    override suspend fun saveAllData(allData: Map<UUID, PlayerData>) =
        allData.forEach { (uuid, playerData) -> setPlayerData(uuid, playerData) }

    /**
     * Set the [PlayerData] for a player.
     *
     * @param uuid the uuid of the player
     * @param playerData the relevant playerdata
     */
    override suspend fun setPlayerData(uuid: UUID, playerData: PlayerData) {
        val query =
            """
                INSERT INTO `moducore`.`player_data` (`uuid`, `nickname`, `balance`, `chatcolor`, `chatpingsenabled`,
                 `homes`, `kit_claim_times`, `discordid`) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                `nickname`=?, `balance`=?, `chatcolor`=?, `chatpingsenabled`=?, `homes`=?, `kit_claim_times`=?, 
                `discordid`=?;
            """.trimIndent()

        hikariDataSource.connection.use { con ->
            con.prepareStatement(query).use { ps ->
                ps.setString(1, uuid.toString())
                ps.setString(2, playerData.nickName)
                ps.setDouble(3, playerData.balance)
                ps.setString(4, playerData.chatColor)
                ps.setBoolean(5, playerData.chatPingsEnabled)
                ps.setString(6, gson.toJson(playerData.homes))
                ps.setString(7, gson.toJson(playerData.kitClaimTimes))
                ps.setLong(8, playerData.discordID ?: 0L)
                // duplicate key
                ps.setString(9, playerData.nickName)
                ps.setDouble(10, playerData.balance)
                ps.setString(11, playerData.chatColor)
                ps.setBoolean(12, playerData.chatPingsEnabled)
                ps.setString(13, gson.toJson(playerData.homes))
                ps.setString(14, gson.toJson(playerData.kitClaimTimes))
                ps.setLong(15, playerData.discordID ?: 0L)
                ps.executeUpdate()
            }
        }
    }
}