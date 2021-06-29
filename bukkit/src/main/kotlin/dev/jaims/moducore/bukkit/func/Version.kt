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

package dev.jaims.moducore.bukkit.func

import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.google.gson.JsonObject
import dev.jaims.mcutils.bukkit.util.log
import dev.jaims.moducore.bukkit.ModuCore
import javax.print.attribute.standard.Severity

fun getLatestVersion(resourceId: Int): String? {
    // get the data
    val (_, response, result) = "https://api.spiget.org/v2/resources/$resourceId/versions/latest".httpGet()
        .responseObject<JsonObject>()
    // if response isn't 200 null
    if (response.statusCode != 200) return null
    val (payload, _) = result
    return payload?.get("name")?.asString
}

fun newerAvailabeVersion(current: List<Int>, latest: List<Int>): Boolean {
    val currentMajor = current.getOrNull(0) ?: return false
    val currentMinor = current.getOrNull(1) ?: return false
    val currentPatch = current.getOrNull(2) ?: return false
    val latestMajor = latest.getOrNull(0) ?: return false
    val latestMinor = latest.getOrNull(1) ?: return false
    val latestPatch = latest.getOrNull(2) ?: return false
    if (latestMajor > currentMajor) return true
    else {
        if (latestMinor > currentMinor) return true
        else {
            if (latestPatch > currentPatch) return true
        }
    }
    return false
}

/**
 * Check the latest version and alert the servers console if it isn't the latest.
 */
fun notifyVersion(plugin: ModuCore) {
    try {
        val currentVersion =
            plugin.description.version.withoutBuildNumber.replace("v", "").split(".").map { it.toInt() }
        val latestVersion =
            getLatestVersion(plugin.resourceId)?.withoutBuildNumber?.replace("v", "")?.split(".")?.map { it.toInt() }
                ?: return
        if (newerAvailabeVersion(currentVersion, latestVersion)) {
            ("There is a new version of ModuCore Available (${latestVersion.joinToString(".")})! " +
                    "Please download it from https://www.spigotmc.org/resources/${plugin.resourceId}/")
                .log(Severity.WARNING)
        }
    } catch (ignored: Throwable) {
        // TODO contact kotlin-fuel about the error that sometimes occurs.
    }
}

private val String.withoutBuildNumber: String
    get() = replace("-b\\d+".toRegex(), "")
