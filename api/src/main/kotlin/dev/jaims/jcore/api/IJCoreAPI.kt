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

package dev.jaims.jcore.api

import dev.jaims.jcore.api.manager.IPlayerManager
import dev.jaims.jcore.api.manager.IPlaytimeManager
import dev.jaims.jcore.api.manager.IStorageManager
import org.bukkit.entity.Player

interface IJCoreAPI {

    /**
     * Allows for a static instance of the API.
     *
     * @sample dev.jaims.jcore.example.ExamplePlugin
     */
    companion object {

        /**
         * An instance of the [IJCoreAPI] - See the sample for how to obtain an instance.
         *
         * @sample dev.jaims.jcore.example.ExamplePlugin
         */
        lateinit var instance: IJCoreAPI
    }

    /**
     * Manages all the [Player] related methods.
     */
    val playerManager: IPlayerManager

    /**
     * Manages all methods related to playtime.
     */
    val playtimeManager: IPlaytimeManager

    /**
     * Manages all storage related methods
     */
    val storageManager: IStorageManager

}