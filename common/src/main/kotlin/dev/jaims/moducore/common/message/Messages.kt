package dev.jaims.moducore.common.message

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun String.bukkitColorize() = LegacyComponentSerializer.legacyAmpersand()