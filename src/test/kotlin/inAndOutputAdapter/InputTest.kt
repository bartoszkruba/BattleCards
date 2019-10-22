package inAndOutputAdapter

import Game
import factory.DeckFactory
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import prototype.CardLoader

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
    fun readCardToPlaceOnField(){
        val cardLoader = CardLoader()

        val deckPrototype = cardLoader.loadDeck("Standard")

        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)
        val game = Game(playerOneDeck, playerTwoDeck, "rami", "noha")

        game.drawCardFromDeck()
        game.drawCardFromDeck()
        game.drawCardFromDeck()
        val whiteHand= game.whitePlayer.hand //contain 3 cards in this case
        val blackHand= game.blackPlayer.hand //contain 0 cards in this case

        //if the hand has the number/name of the card and
        assertNotNull(Input.readCardToPlaceOnField("1", whiteHand))
        assertNotNull(Input.readCardToPlaceOnField("2", whiteHand))
        assertNotNull(Input.readCardToPlaceOnField("3", whiteHand))

        assertNull(Input.readCardToPlaceOnField("1", blackHand))
        assertNull(Input.readCardToPlaceOnField("2", blackHand))
    }

    @Test
    fun readlistAvailableDecks(){
        val decksOption = mutableMapOf(1 to "test", 2 to "Demons", 3 to "Standard", 4 to "Rats")

        assertNotNull(Input.readlistAvailableDecks("test", decksOption))
        assertNotNull(Input.readlistAvailableDecks("Demons", decksOption))
        assertNotNull(Input.readlistAvailableDecks("Standard", decksOption))
        assertNotNull(Input.readlistAvailableDecks("Rats", decksOption))

        assertNotNull(Input.readlistAvailableDecks("1", decksOption))
        assertNotNull(Input.readlistAvailableDecks("2", decksOption))
        assertNotNull(Input.readlistAvailableDecks("3", decksOption))
        assertNotNull(Input.readlistAvailableDecks("4", decksOption))

        assertNull(Input.readlistAvailableDecks("someDeckOption", decksOption))
        assertNull(Input.readlistAvailableDecks("5", decksOption))
        assertNull(Input.readlistAvailableDecks("0", decksOption))
        assertNull(Input.readlistAvailableDecks("rats", decksOption))
        assertNull(Input.readlistAvailableDecks("RATS", decksOption))

    }
}