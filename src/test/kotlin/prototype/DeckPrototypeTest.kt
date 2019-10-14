package prototype

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class DeckPrototypeTest {

    companion object {
        // todo load info from config

        private const val MAX_DECK_SIZE = 30
        private const val MAX_HEALTH = 10
        private const val MAX_ATTACK = 10
        private const val MAX_NAME_LENGTH = 9
        private const val MAX_DECK_NAME_LENGTH = 20
        private const val MIN_DECK_NAME_LENGTH = 1
        private const val ID_ONE = 1
        private const val ID_TWO = 2
        private const val ID_THREE = 3
    }

    private val DECK_NAME_ONE: String
    private val DECK_NAME_TWO: String
    private val DECK_NAME_THREE: String

    private val MONSTER_NAME_ONE: String
    private val MONSTER_NAME_TWO: String
    private val MONSTER_NAME_THREE: String

    init {
        val stringBuilderA = StringBuilder()
        val stringBuilderB = StringBuilder()
        val stringBuilderC = StringBuilder()
        val stringBuilderD = StringBuilder()
        val stringBuilderE = StringBuilder()
        val stringBuilderF = StringBuilder()

        repeat(MAX_DECK_NAME_LENGTH) {
            stringBuilderA.append("a")
            stringBuilderB.append("b")
            stringBuilderC.append("c")
        }

        repeat(MAX_NAME_LENGTH) {
            stringBuilderD.append("d")
            stringBuilderE.append("e")
            stringBuilderF.append("f")
        }

        DECK_NAME_ONE = stringBuilderA.toString()
        DECK_NAME_TWO = stringBuilderB.toString()
        DECK_NAME_THREE = stringBuilderC.toString()

        MONSTER_NAME_ONE = stringBuilderD.toString()
        MONSTER_NAME_TWO = stringBuilderE.toString()
        MONSTER_NAME_THREE = stringBuilderF.toString()
    }

    @Test
    internal fun `Constructor one goes right`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        assertEquals(DECK_NAME_ONE, deckPrototype.name)
        assertEquals(0, deckPrototype.size)
    }

    @Test
    internal fun `Constructor one invalid name`() = shouldThrowRuntimeException(Executable {
        DeckPrototype("£#__.!23?{%&<>")
    })


    @Test
    internal fun `Constructor one name too short`() = shouldThrowRuntimeException(Executable {
        DeckPrototype("")
    })


    @Test
    internal fun `Constructor one name too long`() = shouldThrowRuntimeException(Executable {
        DeckPrototype(DECK_NAME_ONE + "a")
    })

    @Test
    internal fun `Add card goes right`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)

        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        assertTrue(deckPrototype.addCard(prototypeOne))
        assertEquals(1, deckPrototype.size)

        val prototypeTwo = MonsterPrototype(ID_TWO, MONSTER_NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        assertTrue(deckPrototype.addCard(prototypeTwo))
        assertEquals(2, deckPrototype.size)
        assertTrue(deckPrototype.addCard(prototypeTwo))
        assertEquals(3, deckPrototype.size)
    }

    @Test
    internal fun `Add card too many cards`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        repeat(MAX_DECK_SIZE) { assertTrue(deckPrototype.addCard(prototypeOne)) }
        assertFalse(deckPrototype.addCard(prototypeOne))
    }

    @Test
    internal fun `Remove card goes right`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        deckPrototype.addCard(prototypeOne)
        assertEquals(1, deckPrototype.size)
        assertTrue(deckPrototype.removeCard(prototypeOne))
        assertEquals(0, deckPrototype.size)

        assertTrue(deckPrototype.cards[prototypeOne] == null)
    }

    @Test
    internal fun `Remove card, card do not exist`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val prototypeTwo = MonsterPrototype(ID_TWO, MONSTER_NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        assertTrue(deckPrototype.addCard(prototypeOne))
        assertFalse(deckPrototype.removeCard(prototypeTwo))
        assertEquals(1, deckPrototype.size)
    }

    @Test
    internal fun `Remove card by id goes right`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        deckPrototype.addCard(prototypeOne)
        assertEquals(1, deckPrototype.size)
        assertTrue(deckPrototype.removeCard(ID_ONE))
        assertEquals(0, deckPrototype.size)
    }

    @Test
    internal fun `Remove card by id, id do not exists`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        deckPrototype.addCard(prototypeOne)
        assertEquals(1, deckPrototype.size)
        assertTrue(deckPrototype.removeCard(ID_TWO))
        assertEquals(1, deckPrototype.size)
    }

    @Test
    internal fun `cardList test`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val prototypeTwo = MonsterPrototype(ID_TWO, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        assertTrue(deckPrototype.addCard(prototypeOne))
        assertTrue(deckPrototype.addCard(prototypeOne))
        assertEquals(2, deckPrototype.size)

        assertTrue(deckPrototype.addCard(prototypeTwo))
        assertEquals(1, deckPrototype.size)

        val cards = deckPrototype.cards
        assertEquals(2, cards.size)
        assertEquals(2, cards[prototypeOne])
        assertEquals(1, cards[prototypeTwo])

        val key = cards.keys.findLast { k -> k.id == prototypeOne.id }!!

        assertFalse(key === prototypeOne)
    }

    @Test
    internal fun `toString test`() {
        val testString = """
            $MONSTER_NAME_ONE: x2
            $MONSTER_NAME_TWO: x1
        """.trimIndent()

        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val prototypeTwo = MonsterPrototype(ID_TWO, MONSTER_NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        repeat(2) { deckPrototype.addCard(prototypeOne) }
        deckPrototype.addCard(prototypeTwo)

        assertEquals(testString, deckPrototype.toString())
    }

    private fun shouldThrowRuntimeException(executable: Executable) {
        Assertions.assertThrows(RuntimeException::class.java, executable)
        return Unit
    }
}