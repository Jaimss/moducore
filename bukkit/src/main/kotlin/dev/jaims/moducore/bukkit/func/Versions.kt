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

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.common.func.getLatestVersion
import dev.jaims.moducore.common.func.newerAvailabeVersion
import org.json.JSONObject
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse


/**
 * Check the latest version and alert the servers console if it isn't the latest.
 */
fun notifyVersion(plugin: ModuCore) {
    val currentVersion = plugin.description.version.withoutBuildNumber
        .replace("v", "").split(".").map { it.toInt() }
    val latestVersion = getLatestVersion(plugin.resourceId)?.withoutBuildNumber
        ?.replace("v", "")?.split(".")?.map { it.toInt() } ?: return

    if (newerAvailabeVersion(currentVersion, latestVersion)) {
        plugin.logger.info(
            "There is a new version of ModuCore Available (${latestVersion.joinToString(".")})! " +
                    "Please download it from https://www.spigotmc.org/resources/${plugin.resourceId}/"
        )
    }
}

private val String.withoutBuildNumber: String
    get() = replace("-b\\d+".toRegex(), "")
