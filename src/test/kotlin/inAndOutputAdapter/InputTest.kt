package inAndOutputAdapter

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class InputTest {

    @Test
    fun userNameInput() {
        assertNotNull(Input.readName("name"))
        assertNotNull(Input.readName("NAME"))
        assertNotNull(Input.readName("nWithöåä"))
        assertNotNull(Input.readName("NWITHÖÅÄ"))
        assertNull(Input.readName(""))
        assertNull(Input.readName("1"))
        assertNull(Input.readName("1234"))
        assertNull(Input.readName("name more than nine characters"))
    }

    @Test
    fun readGameOptions(){
        val gameOptions= mapOf( 1 to "Draw", 2 to "PUT",3 to "Attack", 4 to "Pass")

        assertNotNull(Input.readGameOptions("1", gameOptions))
        assertNotNull(Input.readGameOptions("Draw", gameOptions))
        assertNotNull(Input.readGameOptions("DRAW", gameOptions))
        assertNotNull(Input.readGameOptions("put", gameOptions))
        assertNotNull(Input.readGameOptions("draw", gameOptions))

        assertNull(Input.readGameOptions("someOption", gameOptions))
        assertNull(Input.readGameOptions("5", gameOptions))
        assertNull(Input.readGameOptions("0", gameOptions))
        assertNull(Input.readGameOptions("-2", gameOptions))


    }
}