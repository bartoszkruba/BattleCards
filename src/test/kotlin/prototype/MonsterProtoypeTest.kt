package prototype

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

import kotlin.RuntimeException

internal class MonsterPrototypeTest {

    companion object {
        private const val NAME = "name"
        private const val ID = 1

        private const val MAX_HEALTH = Settings.MAX_HEALTH
        private const val MIN_HEALTH = Settings.MIN_HEALTH
        private const val MAX_ATTACK = Settings.MAX_DAMAGE
        private const val MIN_ATTACK = Settings.MIN_DAMAGE
        private const val MAX_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        private const val MIN_NAME_LENGTH = Settings.MIN_CARD_NAME_LENGTH
    }

    @Test
    internal fun `Constructor works as it should`() {
        val monsterPrototype = MonsterPrototype(ID, NAME, MIN_HEALTH, MIN_ATTACK)

        assertEquals(NAME, monsterPrototype.name)
        assertEquals(ID, monsterPrototype.id)
        assertEquals(MIN_HEALTH, monsterPrototype.baseHealth)
        assertEquals(MIN_HEALTH, monsterPrototype.baseAttack)
        assertEquals(CardType.MONSTER, monsterPrototype.type)
    }

    @Test
    internal fun `Equals method returns true`() {
        val monsterOne = MonsterPrototype(ID, "o ne", MAX_HEALTH, MAX_ATTACK)

        val monsterTwo = MonsterPrototype(ID, "two", MAX_HEALTH, MAX_ATTACK)

        assertEquals(monsterOne, monsterTwo)
        assertEquals(monsterTwo, monsterOne)
    }


    @Test
    internal fun `Equals method returns false`() {
        val monsterOne = MonsterPrototype(1, NAME, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(2, NAME, MAX_HEALTH, MAX_ATTACK)


        assertNotEquals(monsterOne, monsterTwo)
        assertNotEquals(monsterTwo, monsterOne)
    }

    @Test
    internal fun `Constructor with too high base health throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, NAME, MAX_HEALTH + 1, MAX_ATTACK)
    })

    @Test
    internal fun `Constructor with too high attack throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, NAME, MAX_HEALTH, MAX_ATTACK + 1)
    })

    @Test
    internal fun `Constructor with too low attack throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, NAME, MAX_HEALTH, MIN_ATTACK - 1)
    })

    @Test
    internal fun `Constructor with too low base health throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, NAME, MIN_HEALTH - 1, MIN_ATTACK)
    })

    @Test
    internal fun `Constructor with too short name throws exception`() = shouldThrowRuntimeException(Executable {
        val stringBuilder = StringBuilder()
        repeat(MIN_NAME_LENGTH - 1) { stringBuilder.append("a") }
        val name = stringBuilder.toString()

        MonsterPrototype(ID, name, MAX_HEALTH, MAX_ATTACK)
    })

    @Test
    internal fun `Constructor with too long name throws exception`() = shouldThrowRuntimeException(Executable {
        val stringBuilder = StringBuilder()
        repeat(MAX_NAME_LENGTH + 1) { stringBuilder.append("a") }
        val name = stringBuilder.toString()

        MonsterPrototype(ID, name, MAX_HEALTH, MAX_ATTACK)
    })

    @Test
    internal fun `Constructor with invalid name throws exception`() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, " 2#_?!<.)", MAX_HEALTH, MAX_ATTACK)
    })

    // todo test equals with other CardPrototype classes

    @Test
    internal fun `Test clone`() {
        val original = MonsterPrototype(ID, NAME, MAX_HEALTH, MAX_ATTACK)
        val copy = original.copy()

        assertTrue(original == copy)
        assertFalse(original === copy)
        assertEquals(ID, copy.id)
        assertEquals(NAME, copy.name)
        assertEquals(MAX_HEALTH, copy.baseHealth)
        assertEquals(MAX_ATTACK, copy.baseAttack)
        assertEquals(CardType.MONSTER, copy.type)
    }

    private fun shouldThrowRuntimeException(executable: Executable) {
        assertThrows(RuntimeException::class.java, executable)
        return Unit
    }
}