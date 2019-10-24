package models

import Card
import Monster
import Settings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DeckTest {

    companion object {
        const val MAX_ATTACK = Settings.MAX_DAMAGE
        const val MAX_HEALTH = Settings.MAX_HEALTH
        const val MAX_CARD_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
    }

    val MONSTER_NAME_ONE: String
    val MONSTER_NAME_TWO: String
    val MONSTER_NAME_THREE: String

    init {
        val sbA = StringBuilder()
        val sbB = StringBuilder()
        val sbC = StringBuilder()

        repeat(MAX_CARD_NAME_LENGTH) {
            sbA.append("a")
            sbB.append("b")
            sbC.append("c")
        }

        MONSTER_NAME_ONE = sbA.toString()
        MONSTER_NAME_TWO = sbB.toString()
        MONSTER_NAME_THREE = sbC.toString()
    }

    @Test
    internal fun `shuffle() test`() {
        val monsterOne = Monster(name = MONSTER_NAME_ONE, attack = MAX_ATTACK, health = MAX_HEALTH)
        val monsterTwo = Monster(name = MONSTER_NAME_TWO, attack = MAX_ATTACK, health = MAX_HEALTH)
        val monsterThree = Monster(name = MONSTER_NAME_THREE, attack = MAX_ATTACK, health = MAX_HEALTH)

        val deck = Deck()

        repeat(10) {
            deck.addCard(monsterOne)
            deck.addCard(monsterTwo)
            deck.addCard(monsterThree)
        }

        assertFalse(deck.empty)

        val cardsBeforeShuffle = ArrayList(deck.cardsInList())

        deck.shuffleDeck()
        val cardsAfterShuffle = deck.cardsInList()

        assertFalse(listsExactlySame(cardsBeforeShuffle, cardsAfterShuffle))
    }

    private fun listsExactlySame(listOne: List<Card>, listTwo: List<Card>): Boolean {
        for (i in listOne.indices) if (listOne[i] != listTwo[i]) return false

        return true
    }

    @Test
    internal fun `drawCard() test`() {
        val monsterOne = Monster(name = MONSTER_NAME_ONE, attack = MAX_ATTACK, health = MAX_HEALTH)
        val monsterTwo = Monster(name = MONSTER_NAME_TWO, attack = MAX_ATTACK, health = MAX_HEALTH)
        val monsterThree = Monster(name = MONSTER_NAME_THREE, attack = MAX_ATTACK, health = MAX_HEALTH)

        val deck = Deck()

        deck.addCard(monsterOne)
        deck.addCard(monsterTwo)
        deck.addCard(monsterThree)

        assertFalse(deck.empty)
        assertEquals(3, deck.cardsInList().size)

        val cardOne = deck.drawCard()
        assertNotNull(cardOne)
        assertEquals(monsterOne, cardOne)
        assertFalse(deck.empty)
        assertEquals(2, deck.cardsInList().size)

        val cardTwo = deck.drawCard()
        assertNotNull(cardTwo)
        assertEquals(monsterTwo, cardTwo)
        assertFalse(deck.empty)
        assertEquals(1, deck.cardsInList().size)

        val cardThree = deck.drawCard()
        assertNotNull(cardThree)
        assertEquals(monsterThree, cardThree)
        assertTrue(deck.empty)
        assertEquals(0, deck.cardsInList().size)
    }

    @Test
    internal fun `drawCard() test, no cards left`() {
        val deck = Deck()

        val card = deck.drawCard()
        assertNull(card)
        assertTrue(deck.empty)
        assertEquals(0, deck.cardsInList().size)
    }
}