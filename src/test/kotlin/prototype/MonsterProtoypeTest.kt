package prototype

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

import kotlin.RuntimeException

internal class MonsterPrototypeTest {

    companion object {
        private const val ID_ONE = 1
        private const val ID_TWO = 2

        private const val MAX_HEALTH = Settings.MAX_HEALTH
        private const val MIN_HEALTH = Settings.MIN_HEALTH
        private const val MAX_ATTACK = Settings.MAX_DAMAGE
        private const val MIN_ATTACK = Settings.MIN_DAMAGE
        private const val MAX_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        private const val MIN_NAME_LENGTH = Settings.MIN_CARD_NAME_LENGTH
    }

    private val NAME_ONE: String
    private val NAME_TWO: String

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
    internal fun `Constructor works as it should`() {
        val monsterPrototype = MonsterPrototype(ID_ONE, NAME_ONE, MIN_HEALTH, MIN_ATTACK)

        assertEquals(NAME_ONE, monsterPrototype.name)
        assertEquals(ID_ONE, monsterPrototype.id)
        assertEquals(MIN_HEALTH, monsterPrototype.baseHealth)
        assertEquals(MIN_HEALTH, monsterPrototype.baseAttack)
        assertEquals(CardType.MONSTER, monsterPrototype.type)
    }

    @Test
    internal fun `Equals method returns true`() {
        val monsterOne = MonsterPrototype(ID_ONE, "o ne", MAX_HEALTH, MAX_ATTACK)

        val monsterTwo = MonsterPrototype(ID_ONE, "two", MAX_HEALTH, MAX_ATTACK)

        assertEquals(monsterOne, monsterTwo)
        assertEquals(monsterTwo, monsterOne)
    }


    @Test
    internal fun `Equals method returns false`() {
        val monsterOne = MonsterPrototype(1, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(2, NAME_ONE, MAX_HEALTH, MAX_ATTACK)


        assertNotEquals(monsterOne, monsterTwo)
        assertNotEquals(monsterTwo, monsterOne)
    }

    @Test
    internal fun `Constructor with too high base health throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH + 1, MAX_ATTACK)
    })

    @Test
    internal fun `Constructor with too high attack throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK + 1)
    })

    @Test
    internal fun `Constructor with too low attack throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MIN_ATTACK - 1)
    })

    @Test
    internal fun `Constructor with too low base health throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID_ONE, NAME_ONE, MIN_HEALTH - 1, MIN_ATTACK)
    })

    @Test
    internal fun `Constructor with too short name throws exception`() = shouldThrowRuntimeException(Executable {
        val stringBuilder = StringBuilder()
        repeat(MIN_NAME_LENGTH - 1) { stringBuilder.append("a") }
        val name = stringBuilder.toString()

        MonsterPrototype(ID_ONE, name, MAX_HEALTH, MAX_ATTACK)
    })

    @Test
    internal fun `Constructor with too long name throws exception`() = shouldThrowRuntimeException(Executable {
        val stringBuilder = StringBuilder()
        repeat(MAX_NAME_LENGTH + 1) { stringBuilder.append("a") }
        val name = stringBuilder.toString()

        MonsterPrototype(ID_ONE, name, MAX_HEALTH, MAX_ATTACK)
    })

    @Test
    internal fun `Constructor with invalid name throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID_ONE, " 2#_?!<.)", MAX_HEALTH, MAX_ATTACK)
    })

    @Test
    internal fun `Test equals with SpellPrototype, returns true`() {
        val monster = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val spell = SpellPrototype(ID_ONE, NAME_TWO)

        assertTrue(monster.equals(spell))
    }

    @Test
    internal fun `Test equals with SpellPrototype, return false`() {
        val monster = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val spell = SpellPrototype(ID_TWO, NAME_ONE)

        assertFalse(monster.equals(spell))
    }

    @Test
    internal fun `Test equals with another class`() {
        val monster = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val string = ""

        assertFalse(monster.equals(string))
    }

    @Test
    internal fun `Test clone`() {
        val original = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val copy = original.copy()

        assertTrue(original == copy)
        assertFalse(original === copy)
        assertEquals(ID_ONE, copy.id)
        assertEquals(NAME_ONE, copy.name)
        assertEquals(MAX_HEALTH, copy.baseHealth)
        assertEquals(MAX_ATTACK, copy.baseAttack)
        assertEquals(CardType.MONSTER, copy.type)
    }

    private fun shouldThrowRuntimeException(executable: Executable) {
        assertThrows(RuntimeException::class.java, executable)
        return Unit
    }
}