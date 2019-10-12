import Models.Deck
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.Exception
import java.lang.RuntimeException

internal class CardListTest {

    @Test
    internal fun removeCardTest() {
        var pigMonster: Card = Monster("Pig")
        var rabbitMonster: Card = Monster("Rabbit")
        var deck: Deck = Deck(false, arrayListOf(pigMonster))

        var removedCard: Card = Monster()

        try {
            removedCard = deck.removeCard(pigMonster)
        } catch (error: Exception) {
            assertTrue(false, "Something went wrong when trying to remove a card")
        }

        assertEquals(pigMonster, removedCard, "The removed card doesn't match the card we wanted to remove")
        assertEquals(0, deck.cards.size, "The card did not get removed, a card still exists in the deck")
        assertEquals(true, deck.empty, "The boolean empty should be set to true because no cards exists")

        deck = Deck(false, arrayListOf(rabbitMonster, pigMonster))

        try {
            removedCard = deck.removeCard(rabbitMonster)
        } catch (error: Exception) {
            assertTrue(false, "Something went wrong when trying to remove a card")
        }

        assertEquals(rabbitMonster, removedCard, "The removed card doesn't match the card we wanted to remove")
        assertEquals(1, deck.cards.size, "The card did not get removed, card array size is not correct")
        assertEquals(pigMonster, deck.cards[0],"The card that should exist is not the on existing")

        try {
            deck.removeCard(rabbitMonster)
            assertTrue(false, "Trying to remove a card that does not exist,should throw runtimeException")
        } catch (error: RuntimeException) {}
    }
}