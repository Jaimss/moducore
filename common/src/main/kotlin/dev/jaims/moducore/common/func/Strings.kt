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

package dev.jaims.moducore.common.func

import dev.jaims.moducore.common.const.InputType
import org.json.JSONObject
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

/**
 * Post a [String] to https://paste.jaims.dev. Useful for sending console logs or error messages.
 * Splits \n for new lines
 */
fun String.toPastebin(): String {
    val url = "https://paste.jaims.dev/documents/"
    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json; charset=utf-8")
        .POST(HttpRequest.BodyPublishers.ofString(this))
        .build()

    val response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString())
    val json = JSONObject(response.body())
    val key = json.getString("key")
    return "https://paste.jaims.dev/${key}"
}

/**
 * Get the UUID of a Name
 *
 * @return the [UUID]
 */
fun String.getUUID(): UUID? {
    val url = "https://playerdb.co/api/player/minecraft/$this"
    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build()

    val response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString())
    val json = JSONObject(response.body())

    return if (!json.getBoolean("success")) null
    else {
        val playerObj: JSONObject = json.getJSONObject("data").getJSONObject("player")
        UUID.fromString(playerObj.getString("id"))
    }
}

/**
 * @return what type of [InputType] a certain string is
 */
fun String.getInputType(): InputType {
    if (matches("[A-Za-z\\d]{8}-[A-Za-z\\d]{4}-[A-Za-z\\d]{4}-[A-Za-z\\d]{4}-[A-Za-z\\d]{12}".toRegex()))
        return InputType.UUID
    if (matches("[A-Za-z\\d]{32}".toRegex())) return InputType.SHORTUUID
    return InputType.NAME
}

/**
 * Turn a short UUID (no dashes) into the long UUID format
 */
fun String.asLongUUID(): String = StringBuilder(this).insert(8, "-")
    .insert(13, "-")
    .insert(18, "-")
    .insert(23, "-")
    .toString()
