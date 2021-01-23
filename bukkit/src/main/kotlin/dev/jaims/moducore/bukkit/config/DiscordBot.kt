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

package dev.jaims.moducore.bukkit.config

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object DiscordBot : SettingsHolder {

    @Path("discord-bot-token")
    val TOKEN = Property.create("your_token_here")

    @Comment("Valid types are `watching`, `playing`, `streaming`, `listening`, `competing`")
    @Path("activity.type")
    val ACTIVITY_TYPE = Property.create("watching")

    @Path("activity.description")
    val ACTIVITY_DESCRIPTION = Property.create("Your Server")

    @Comment("The Stream URL. Only applies if `activity.type` is streaming.")
    @Path("activity.stream_url")
    val ACTIVITY_STREAM_URL = Property.create("https://jaims.dev")

    @Comment("Leave this as blank or an invalid id if you don't want chat sent to discord.")
    @Path("channels.chat_id")
    val CHANNEL_CHAT = Property.create("")

    @Comment("Leave this as blank if you don't want some certain updates sent to your admin discord channel.")
    @Path("channels.admin_id")
    val CHANNEL_ADMIN = Property.create("")

}