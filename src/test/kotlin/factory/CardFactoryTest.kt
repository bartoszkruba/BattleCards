package factory

import Monster
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import prototype.MonsterPrototype

internal class CardFactoryTest {

    companion object {
        private const val MAX_CARD_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        private const val MAX_ATTACK = Settings.MAX_DAMAGE
        private const val MIN_ATTACK = Settings.MIN_DAMAGE
        private const val MAX_HEALTH = Settings.MAX_HEALTH
        private const val MIN_HEALTH = Settings.MIN_HEALTH

        private const val ID_ONE = 1
        private const val ID_TWO = 2
        private const val ID_THREE = 3
        private const val ID_FOUR = 4
    }

    private val MONSTER_NAME_ONE: String
    private val MONSTER_NAME_TWO: String
    private val MONSTER_NAME_THREE: String
    private val MONSTER_NAME_FOUR: String


    init {
        val sbA = StringBuilder()
        val sbB = StringBuilder()
        val sbC = StringBuilder()
        val sbD = StringBuilder()

        repeat(MAX_CARD_NAME_LENGTH) {
            sbA.append("A")
            sbB.append("B")
            sbC.append("C")
            sbD.append("D")
        }

        MONSTER_NAME_ONE = sbA.toString()
        MONSTER_NAME_TWO = sbB.toString()
        MONSTER_NAME_THREE = sbC.toString()
        MONSTER_NAME_FOUR = sbD.toString()
    }

    @Test
    internal fun `createCard() test`() {
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val prototypeTwo = MonsterPrototype(ID_TWO, MONSTER_NAME_TWO, MIN_HEALTH, MIN_ATTACK)
        val prototypeThree = MonsterPrototype(ID_THREE, MONSTER_NAME_THREE, MAX_HEALTH, MIN_ATTACK)
        val prototypeFour = MonsterPrototype(ID_FOUR, MONSTER_NAME_FOUR, MIN_HEALTH, MAX_ATTACK)

        val cardOne = CardFactory.createCard(prototypeOne) as Monster
        val cardTwo = CardFactory.createCard(prototypeTwo) as Monster
        val cardThree = CardFactory.createCard(prototypeThree) as Monster
        val cardFour = CardFactory.createCard(prototypeFour) as Monster

        assertEquals(MONSTER_NAME_ONE, cardOne.name)
        assertEquals(MAX_HEALTH, cardOne.health)
        assertEquals(MAX_ATTACK, cardOne.attack)
        assertEquals(CardType.MONSTER, cardOne.type)

        assertEquals(MONSTER_NAME_TWO, cardTwo.name)
        assertEquals(MIN_HEALTH, cardTwo.health)
        assertEquals(MIN_ATTACK, cardTwo.attack)
        assertEquals(CardType.MONSTER, cardTwo.type)

        assertEquals(MONSTER_NAME_THREE, cardThree.name)
        assertEquals(MAX_HEALTH, cardThree.health)
        assertEquals(MIN_ATTACK, cardThree.attack)
        assertEquals(CardType.MONSTER, cardThree.type)

        assertEquals(MONSTER_NAME_FOUR, cardFour.name)
        assertEquals(MIN_HEALTH, cardFour.health)
        assertEquals(MIN_ATTACK, cardFour.attack)
        assertEquals(CardType.MONSTER, cardFour.type)
    }
}