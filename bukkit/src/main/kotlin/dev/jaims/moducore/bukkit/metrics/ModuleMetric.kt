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

package dev.jaims.moducore.bukkit.metrics

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.libs.org.bstats.bukkit.Metrics
import me.mattstudios.config.properties.Property
import java.util.concurrent.Callable

fun Metrics.moduleMetric(plugin: ModuCore): Metrics {
    val chart = Metrics.DrilldownPie("modules_enabled", Callable {
        val map: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()

        Modules::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            val property = field.get(this) as? Property<*> ?: return@forEach
            val name = field.name.replace("_", " ").lowercase()
            val entry: MutableMap<String, Int> = mutableMapOf()
            when (plugin.api.bukkitFileManager.modules[property]) {
                true -> entry["enabled"] = 1
                false -> entry["disabled"] = 1
                else -> entry["other"] = 1
            }
            map[name] = entry
        }

        return@Callable map
    })
    addCustomChart(chart)
    return this
}