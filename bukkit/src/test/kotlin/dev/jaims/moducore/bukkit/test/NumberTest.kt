package dev.jaims.moducore.bukkit.test

import dev.jaims.moducore.bukkit.util.getCompactForm
import org.junit.Test
import kotlin.test.assertEquals

class NumberTest {

    @Test
    fun compactFormValidator() {
        assertEquals(1000.0.getCompactForm(), "1k")
        assertEquals(1_200.0.getCompactForm(), "1.2k")
        assertEquals(3_000_100.0.getCompactForm(), "3m")
    }

}