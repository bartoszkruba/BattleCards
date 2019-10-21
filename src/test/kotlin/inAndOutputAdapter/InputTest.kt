package inAndOutputAdapter

import Game
import factory.DeckFactory
import models.Hand
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
        val deckPrototype = cardLoader.loadDeck("test")
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
}