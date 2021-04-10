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

package dev.jaims.moducore.api

import dev.jaims.moducore.api.manager.*
import org.bukkit.entity.Player

/**
 * This is the entry point to the API. Can be accessed with the bukkit service manager or the static singleton.
 */
interface ModuCoreAPI {

    /**
     * Allows for a static instance of the API.
     */
    companion object {

        /**
         * An instance of the [ModuCoreAPI] - See the sample for how to obtain an instance.
         */
        @JvmStatic
        lateinit var instance: ModuCoreAPI
    }

    /**
     * Manages all the [Player] related methods.
     */
    val playerManager: PlayerManager

    /**
     * Manages all methods related to playtime.
     */
    val playtimeManager: PlaytimeManager

    /**
     * Manages all storage related methods
     */
    val storageManager: StorageManager

    /**
     * Manages all economy methods. The vault provider that ModuCore has calls these methods, so you can either use this API or vaults.
     */
    val economyManager: EconomyManager

    /**
     * Manages all the location related things.
     */
    val locationManager: LocationManager

    /**
     * Manages all hologram methods.
     */
    val hologramManager: HologramManager

}