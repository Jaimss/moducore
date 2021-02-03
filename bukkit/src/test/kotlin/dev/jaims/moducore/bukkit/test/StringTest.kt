package dev.jaims.moducore.bukkit.test

import dev.jaims.moducore.bukkit.util.isValidNickname
import org.junit.Test
import kotlin.test.assertEquals

class StringTest {

    @Test
    fun nicknameVaildator() {
        assertEquals("blah".isValidNickname(), true)
        assertEquals("2".isValidNickname(), false)
        assertEquals("xx_james_xx".isValidNickname(), true)
    }

}