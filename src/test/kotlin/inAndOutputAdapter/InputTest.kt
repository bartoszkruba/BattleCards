package inAndOutputAdapter

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class InputTest {

    @Test
    fun userNameInput() {
        var userInput = Input()

        assertNotNull(Input.readName("name"))
        assertNull(Input.readName(""))
        assertNull(Input.readName("1"))
        assertNull(Input.readName("1234"))
        assertNull(Input.readName("name more than nine characters"))
    }

    @Test
    fun deckChoice() {
        var userInput = Input()
        val decks = listOf("one", "two", "three", "four")

        assertTrue(userInput.deckChoice(decks.get(0), 1))
        assertTrue(userInput.deckChoice(decks.get(3), 2))
        assertFalse(userInput.deckChoice("random deck name", 2))
        assertFalse(userInput.deckChoice("two", 0))
        assertTrue(userInput.deckChoice("1", 1))
        assertFalse(userInput.deckChoice("7", 2))
    }

    @Test
    fun menu() {
        var userInput = Input()

        assertNotNull(userInput.menu("2"))
        assertNotNull(userInput.menu("Main Menu"))
        assertNotNull(userInput.menu("1"))
        assertNotNull(userInput.menu("Sub Menu"))
        assertEquals("Sub Menu", userInput.menu("Sub Menu"))
        assertNull(userInput.menu("something"))
        assertNull(userInput.menu("3"))
    }
}