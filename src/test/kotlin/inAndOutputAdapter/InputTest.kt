package inAndOutputAdapter

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class InputTest {

    @Test
    fun userNameInput() {
        var userInput = Input()

        assertTrue(userInput.userNameInput("name", 1))
        assertFalse(userInput.userNameInput("", 0))
        assertFalse(userInput.userNameInput("1234", 3))
        assertFalse(userInput.userNameInput("name77", -1))
        assertFalse(userInput.userNameInput("name more than nine characters", 5))
    }

    @Test
    fun deckNameInput() {
        var userInput = Input()
        val decks = listOf("one", "two", "three", "four")

        assertTrue(userInput.deckNameInput(decks.get(0), 1))
        assertTrue(userInput.deckNameInput(decks.get(3), 2))
        assertFalse(userInput.deckNameInput("random deck name", 2))
        assertFalse(userInput.deckNameInput("name", 0))
        assertTrue(userInput.deckNameInput("1", 1))
        assertTrue(userInput.deckNameInput("7", 2))
    }
}