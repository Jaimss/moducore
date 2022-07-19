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

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta

/**
 * create an [ItemStack] from a [material] with [features]
 */
inline fun createItem(material: Material, features: ItemStack.() -> Unit): ItemStack {
    return ItemStack(material).apply(features)
}

/**
 * create an [ItemStack] from a copy of another [itemStack] with [features]
 */
inline fun createItem(itemStack: ItemStack, features: ItemStack.() -> Unit): ItemStack {
    return ItemStack(itemStack).apply(features)
}

/**
 * Modify the item meta of a [ItemStack]
 */
inline fun ItemStack.meta(meta: ItemMeta.() -> Unit) {
    itemMeta = itemMeta?.apply(meta)
}

/**
 * Repair an [ItemStack]
 */
fun ItemStack?.repair() {
    if (this == null || this.type == Material.AIR) return
    val meta = itemMeta ?: return
    if (meta !is Damageable) return
    meta.damage = 0
    itemMeta = meta
}