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

    @Test
    fun readCardToPlaceOnField(){
        val cardLoader = CardLoader()

        val deckPrototype = cardLoader.loadDeck("test")
        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)

        val game = Game(playerOneDeck, playerTwoDeck, "rami", "noha")

        val playerHand = game.blackPlayer.hand
        var cardsInHand = playerHand.cardsInList()

        //if the hand has the number/name of the card and
        assertNotNull(Input.readCardToPlaceOnField("${1..30}", playerHand))
        assertNotNull(Input.readCardToPlaceOnField("${1..30}", playerHand))

        if (playerHand.size() == 0) assertNull(Input.readCardToPlaceOnField("${1..30}", playerHand))

        //if the hand has not the number of the card
        assertNull(Input.readCardToPlaceOnField("31",playerHand))


    }
}