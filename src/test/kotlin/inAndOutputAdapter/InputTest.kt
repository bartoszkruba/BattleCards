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

        val deckPrototype = cardLoader.loadDeck("test")
        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)

        val game = Game(playerOneDeck, playerTwoDeck, "rami", "noha")

        val playerHand = game.blackPlayer.hand
        var cardsInHand = playerHand.cardsInList()
        println("${1..30}")

        //if the hand has the number/name of the card and
        assertNotNull(Input.readCardToPlaceOnField("${1..30}", playerHand))
        assertNotNull(Input.readCardToPlaceOnField("${1..30}", playerHand))

        if (playerHand.size() == 0) assertNull(Input.readCardToPlaceOnField("${1..30}", playerHand))

        //if the hand has not the number of the card
        assertNull(Input.readCardToPlaceOnField("31",playerHand))


    }
}