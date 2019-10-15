package prototype

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class JsonDeckTest {

    companion object {
        const val MAX_DECK_NAME_LENGTH = Settings.MAX_DECK_NAME_LENGTH
        const val MAX_CARD_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        const val MAX_HEALTH = Settings.MAX_HEALTH
        const val MAX_DAMAGE = Settings.MAX_DAMAGE
        const val DECK_SIZE = Settings.DECK_SIZE

        const val MONSTER_ID_ONE = 2
        const val MONSTER_ID_TWO = 3
        const val MONSTER_ID_THREE = 4
    }

    val MONSTER_NAME_ONE: String
    val MONSTER_NAME_TWO: String
    val MONSTER_NAME_THREE: String
    val DECK_NAME_ONE: String

    init {
        val sbA = StringBuilder()
        val sbB = StringBuilder()
        val sbC = StringBuilder()
        val sbD = StringBuilder()

        repeat(MAX_CARD_NAME_LENGTH) {
            sbA.append("a")
            sbB.append("b")
            sbC.append("c")
        }
        repeat(MAX_DECK_NAME_LENGTH) {
            sbD.append("d")
        }
        MONSTER_NAME_ONE = sbA.toString()
        MONSTER_NAME_TWO = sbB.toString()
        MONSTER_NAME_THREE = sbC.toString()

        DECK_NAME_ONE = sbD.toString()
    }

    @Test
    internal fun `Constructor one test`() {
        val deck = JsonDeck(DECK_NAME_ONE)
        assertEquals(DECK_NAME_ONE, deck.name)
        assertEquals(0, deck.size)
        assertEquals(0, deck.records().size)
    }

    @Test
    internal fun `Constructor one test too long name`() {
        val name = DECK_NAME_ONE + "d"
        shouldThrowRuntimeException(Executable { JsonDeck(name) })
    }

    @Test
    internal fun `Constructor one test invalid name`() {
        val name = "#_!$¤@<4"
        shouldThrowRuntimeException(Executable { JsonDeck(name) })
    }

    @Test
    internal fun `Constructor two test`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        val monsterOne = MonsterPrototype(MONSTER_ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_DAMAGE)
        val monsterTwo = MonsterPrototype(MONSTER_ID_TWO, MONSTER_NAME_TWO, MAX_HEALTH, MAX_DAMAGE)
        val monsterThree = MonsterPrototype(MONSTER_ID_THREE, MONSTER_NAME_THREE, MAX_HEALTH, MAX_DAMAGE)
        repeat(2) {
            deckPrototype.addCard(monsterOne)
            deckPrototype.addCard(monsterTwo)
        }
        deckPrototype.addCard(monsterThree)

        val jsonDeck = JsonDeck(deckPrototype)
        assertEquals(5, jsonDeck.size)
        val records = jsonDeck.records()
        assertEquals(3, records.size)
        assertEquals(2, records[MONSTER_ID_ONE])
        assertEquals(2, records[MONSTER_ID_TWO])
        assertEquals(1, records[MONSTER_ID_THREE])
    }

    @Test
    internal fun `records() test`() {
        val monsterOne = MonsterPrototype(MONSTER_ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_DAMAGE)
        val monsterTwo = MonsterPrototype(MONSTER_ID_TWO, MONSTER_NAME_TWO, MAX_HEALTH, MAX_DAMAGE)
        val monsterThree = MonsterPrototype(MONSTER_ID_THREE, MONSTER_NAME_THREE, MAX_HEALTH, MAX_DAMAGE)

        val jsonDeck = JsonDeck(DECK_NAME_ONE)
        jsonDeck.addCard(monsterOne)
        jsonDeck.addCard(monsterTwo)
        jsonDeck.addCard(monsterThree)

        val recordsOne = jsonDeck.records()
        assertEquals(3, recordsOne.size)
        assertEquals(1, recordsOne[MONSTER_ID_ONE])
        assertEquals(1, recordsOne[MONSTER_ID_TWO])
        assertEquals(1, recordsOne[MONSTER_ID_THREE])

        recordsOne[MONSTER_ID_ONE] = 20

        val recordsTwo = jsonDeck.records()

        assertEquals(3, recordsTwo.size)
        assertEquals(1, recordsTwo[MONSTER_ID_ONE])
        assertEquals(1, recordsTwo[MONSTER_ID_TWO])
        assertEquals(1, recordsTwo[MONSTER_ID_THREE])
    }

    @Test
    internal fun `addCard() test with id`() {
        val jsonDeck = JsonDeck(DECK_NAME_ONE)
        repeat(2) { assertTrue(jsonDeck.addCard(MONSTER_ID_ONE)) }
        assertTrue(jsonDeck.addCard(MONSTER_ID_TWO))
        assertTrue(jsonDeck.addCard(MONSTER_ID_THREE))

        assertEquals(4, jsonDeck.size)

        val records = jsonDeck.records()
        assertEquals(3, records.size)
        assertEquals(2, records[MONSTER_ID_ONE])
        assertEquals(1, records[MONSTER_ID_TWO])
        assertEquals(1, records[MONSTER_ID_THREE])
    }

    @Test
    internal fun `addCard() test with id, to many cards`() {
        val jsonDeck = JsonDeck(DECK_NAME_ONE)
        repeat(DECK_SIZE) { assertTrue(jsonDeck.addCard(MONSTER_ID_ONE)) }
        assertFalse(jsonDeck.addCard(MONSTER_ID_ONE))
        assertEquals(DECK_SIZE, jsonDeck.size)
    }

    @Test
    internal fun `addCard() test with CardPrototype`() {
        val jsonDeck = JsonDeck(DECK_NAME_ONE)
        val monsterOne = MonsterPrototype(MONSTER_ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_DAMAGE)
        val monsterTwo = MonsterPrototype(MONSTER_ID_TWO, MONSTER_NAME_TWO, MAX_HEALTH, MAX_DAMAGE)
        val monsterThree = MonsterPrototype(MONSTER_ID_THREE, MONSTER_NAME_THREE, MAX_HEALTH, MAX_DAMAGE)

        assertTrue(jsonDeck.addCard(monsterOne))
        assertTrue(jsonDeck.addCard(monsterTwo))
        assertTrue(jsonDeck.addCard(monsterThree))

        assertEquals(3, jsonDeck.size)

        val records = jsonDeck.records()
        assertEquals(3, records.size)
        assertEquals(1, records[MONSTER_ID_ONE])
        assertEquals(1, records[MONSTER_ID_TWO])
        assertEquals(1, records[MONSTER_ID_THREE])
    }

    @Test
    internal fun `addCard() test with CardPrototype, too many cards`() {
        val jsonDeck = JsonDeck(DECK_NAME_ONE)

        val monsterOne = MonsterPrototype(MONSTER_ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_DAMAGE)
        repeat(DECK_SIZE) { assertTrue(jsonDeck.addCard(monsterOne)) }

        assertFalse(jsonDeck.addCard(monsterOne))
        assertEquals(DECK_SIZE, jsonDeck.size)
    }

    @Test
    internal fun `removeCard() with id`() {
        val jsonDeck = JsonDeck(DECK_NAME_ONE)
        jsonDeck.addCard(MONSTER_ID_ONE)
        assertEquals(1, jsonDeck.size)

        assertTrue(jsonDeck.removeCard(MONSTER_ID_ONE))
        assertEquals(0, jsonDeck.size)
        val records = jsonDeck.records()
        assertEquals(0, records.size)
        assertNull(records[MONSTER_ID_ONE])
    }

    @Test
    internal fun `removeCard() with id, id not found`() {
        val jsonDeck = JsonDeck(DECK_NAME_ONE)
        jsonDeck.addCard(MONSTER_ID_ONE)
        assertEquals(1, jsonDeck.size)

        assertFalse(jsonDeck.removeCard(MONSTER_ID_TWO))
        assertEquals(1, jsonDeck.size)
        val records = jsonDeck.records()
        assertEquals(1, records.size)
        assertNull(records[MONSTER_ID_TWO])
        assertEquals(1, records[MONSTER_ID_ONE])
    }

    @Test
    internal fun `removeCard() with CardPrototype`() {
        val jsonDeck = JsonDeck(DECK_NAME_ONE)
        val monsterOne = MonsterPrototype(MONSTER_ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_DAMAGE)
        assertTrue(jsonDeck.addCard(monsterOne))
        assertEquals(1, jsonDeck.size)
        assertTrue(jsonDeck.removeCard(monsterOne))
        assertEquals(0, jsonDeck.size)
        val records = jsonDeck.records()
        assertEquals(0, records.size)
    }

    @Test
    internal fun `removeCard() with CardPrototype, id not found`() {
        val jsonDeck = JsonDeck(DECK_NAME_ONE)
        val monsterOne = MonsterPrototype(MONSTER_ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_DAMAGE)
        val monsterTwo = MonsterPrototype(MONSTER_ID_TWO, MONSTER_NAME_TWO, MAX_HEALTH, MAX_DAMAGE)
        assertTrue(jsonDeck.addCard(monsterOne))
        assertEquals(1, jsonDeck.size)
        assertFalse(jsonDeck.removeCard(monsterTwo))
        assertEquals(1, jsonDeck.size)
        val records = jsonDeck.records()
        assertEquals(1, records.size)
    }

    private fun shouldThrowRuntimeException(executable: Executable) {
        assertThrows(RuntimeException::class.java, executable)
        return Unit
    }

    // todo test equals
}
