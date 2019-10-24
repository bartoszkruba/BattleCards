package factory

import Monster
import Spell
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import prototype.MonsterPrototype
import prototype.SpellPrototype

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

    private val NAME_ONE: String
    private val NAME_TWO: String
    private val NAME_THREE: String
    private val NAME_FOUR: String


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

        NAME_ONE = sbA.toString()
        NAME_TWO = sbB.toString()
        NAME_THREE = sbC.toString()
        NAME_FOUR = sbD.toString()
    }

    @Test
    internal fun `createCard() test, monsters`() {
        val prototypeOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val prototypeTwo = MonsterPrototype(ID_TWO, NAME_TWO, MIN_HEALTH, MIN_ATTACK)
        val prototypeThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MIN_ATTACK)
        val prototypeFour = MonsterPrototype(ID_FOUR, NAME_FOUR, MIN_HEALTH, MAX_ATTACK)

        val cardOne = CardFactory.createCard(prototypeOne) as Monster
        val cardTwo = CardFactory.createCard(prototypeTwo) as Monster
        val cardThree = CardFactory.createCard(prototypeThree) as Monster
        val cardFour = CardFactory.createCard(prototypeFour) as Monster

        assertEquals(NAME_ONE, cardOne.name)
        assertEquals(MAX_HEALTH, cardOne.health)
        assertEquals(MAX_ATTACK, cardOne.attack)
        assertEquals(CardType.MONSTER, cardOne.type)

        assertEquals(NAME_TWO, cardTwo.name)
        assertEquals(MIN_HEALTH, cardTwo.health)
        assertEquals(MIN_ATTACK, cardTwo.attack)
        assertEquals(CardType.MONSTER, cardTwo.type)

        assertEquals(NAME_THREE, cardThree.name)
        assertEquals(MAX_HEALTH, cardThree.health)
        assertEquals(MIN_ATTACK, cardThree.attack)
        assertEquals(CardType.MONSTER, cardThree.type)

        assertEquals(NAME_FOUR, cardFour.name)
        assertEquals(MIN_HEALTH, cardFour.health)
        assertEquals(MAX_ATTACK, cardFour.attack)
        assertEquals(CardType.MONSTER, cardFour.type)
    }

    @Test
    internal fun `createCard() test, spells`() {
        val prototypeOne = SpellPrototype(ID_ONE, NAME_ONE)
        val prototypeTwo = SpellPrototype(ID_TWO, NAME_TWO)
        val prototypeThree = SpellPrototype(ID_THREE, NAME_THREE)
        val prototypeFour = SpellPrototype(ID_FOUR, NAME_FOUR)

        val spellOne = CardFactory.createCard(prototypeOne) as Spell
        val spellTwo = CardFactory.createCard(prototypeTwo) as Spell
        val spellThree = CardFactory.createCard(prototypeThree) as Spell
        val spellFour = CardFactory.createCard(prototypeFour) as Spell

        assertEquals(NAME_ONE, spellOne.name)
        assertEquals(CardType.SPEll, spellOne.type)

        assertEquals(NAME_TWO, spellTwo.name)
        assertEquals(CardType.SPEll, spellTwo.type)

        assertEquals(NAME_THREE, spellThree.name)
        assertEquals(CardType.SPEll, spellThree.type)

        assertEquals(NAME_FOUR, spellFour.name)
        assertEquals(CardType.SPEll, spellFour.type)
    }
}