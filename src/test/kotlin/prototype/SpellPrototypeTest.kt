package prototype

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SpellPrototypeTest {

    companion object {
        const val MAX_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        const val MIN_NAME_LENGHT = Settings.MIN_CARD_NAME_LENGTH
        const val MAX_ATTACK = Settings.MAX_DAMAGE
        const val MAX_HEALTH = Settings.MAX_HEALTH

        const val ID_ONE = 1
        const val ID_TWO = 2
    }

    val NAME_ONE: String
    val NAME_TWO: String

    init {
        val sbA = StringBuilder()
        val sbB = StringBuilder()

        repeat(MAX_NAME_LENGTH) {
            sbA.append("a")
            sbB.append("b")
        }

        NAME_ONE = sbA.toString()
        NAME_TWO = sbB.toString()
    }

    @Test
    internal fun `Constructor test`() {
        val spellOne = SpellPrototype(ID_ONE, NAME_ONE)

        assertEquals(NAME_ONE, spellOne.name)
        assertEquals(ID_ONE, spellOne.id)

        val spellTwo = SpellPrototype(ID_TWO, NAME_TWO)

        assertEquals(NAME_TWO, spellTwo.name)
        assertEquals(ID_TWO, spellTwo.id)
    }

    @Test
    internal fun `Create spell with name too short`() {
        val sb = StringBuilder()
        repeat(MIN_NAME_LENGHT - 1) {
            sb.append("a")
        }

        val name = sb.toString()

        assertThrows<RuntimeException> { SpellPrototype(ID_ONE, name) }
    }

    @Test
    internal fun `Create spell with name too long`() {
        val sb = StringBuilder()

        repeat(MAX_NAME_LENGTH + 1) { sb.append("a") }

        val name = sb.toString()
        assertThrows<RuntimeException> {
            SpellPrototype(ID_ONE, name)
        }
    }

    @Test
    internal fun `Create spell with invalid name`() {
        val name = "#¤_.< !?"
        assertThrows<RuntimeException> { SpellPrototype(ID_ONE, name) }
    }

    @Test
    internal fun `clone() test`() {
        val spellPrototype = SpellPrototype(ID_ONE, NAME_ONE)
        val clonedSpell = spellPrototype.clone()

        assertEquals(spellPrototype.id, clonedSpell.id)
        assertEquals(spellPrototype.name, clonedSpell.name)
    }

    @Test
    internal fun `equals() test, returns true`() {
        val spellPrototypeOne = SpellPrototype(ID_ONE, NAME_ONE)
        val spellPrototypeTwo = SpellPrototype(ID_ONE, NAME_TWO)

        assertTrue(spellPrototypeOne == spellPrototypeTwo)
        assertTrue(spellPrototypeTwo == spellPrototypeOne)
    }

    @Test
    internal fun `equals() test, returns false`() {
        val spellPrototypeOne = SpellPrototype(ID_ONE, NAME_ONE)
        val spellPrototypeTwo = SpellPrototype(ID_TWO, NAME_ONE)

        assertFalse(spellPrototypeOne == spellPrototypeTwo)
        assertFalse(spellPrototypeTwo == spellPrototypeOne)
    }

    @Test
    internal fun `equals with MonsterPrototype`() {
        val monster = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val spell = SpellPrototype(ID_ONE, NAME_TWO)

        assertTrue(spell.equals(monster))
    }

    @Test
    internal fun `not equals with MonsterPrototype`() {
        val monster = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val spell = SpellPrototype(ID_TWO, NAME_ONE)

        assertFalse(spell.equals(monster))
    }

    @Test
    internal fun `equals with another object`() {
        val string = ""
        val spell = SpellPrototype(ID_ONE, NAME_TWO)

        assertFalse(spell.equals(string))
    }
}