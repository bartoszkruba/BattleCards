package inAndOutputAdapter

import factory.DeckFactory
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import prototype.CardLoader

internal class GameControllerTest {

    @Test
    fun doTheChoice() {
        val cardLoader = CardLoader()
        val deckPrototype = cardLoader.loadDeck("test")
        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)
        var decks = Pair(playerOneDeck, playerTwoDeck)

        val deckPrototype2 = cardLoader.loadDeck("random json file")
        val playerTwoDeck2 = DeckFactory.createDeck(deckPrototype2)
        var invalidDecks = Pair(playerOneDeck, playerTwoDeck2)

        var gameController = GameController()

        assertNotNull(gameController.doTheChoice("1", decks))
        assertNotNull(gameController.doTheChoice("2", decks))
        assertNotNull(gameController.doTheChoice("3", decks))
        assertNotNull(gameController.doTheChoice("4", decks))
        // a option of a list that does exist
        assertNotNull(gameController.doTheChoice("draw card", decks))
        assertNotNull(gameController.doTheChoice("attack monster", decks))
        assertNotNull(gameController.doTheChoice("place card", decks))
        assertNotNull(gameController.doTheChoice("end round", decks))

        //a number of option that does not exist
        assertNull(gameController.doTheChoice("5", decks))
        //a option written with uppercase letter
        assertNull(gameController.doTheChoice("Draw Card", decks))
        assertNull(gameController.doTheChoice("DRAW CARD", decks))
        assertNull(gameController.doTheChoice("random option", decks))
        //invalid/does not exist one deck out of the 2 decks
        assertNull(gameController.doTheChoice("validOption", invalidDecks))
    }
}