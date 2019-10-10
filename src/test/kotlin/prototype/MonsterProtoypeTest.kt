package prototype

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

import kotlin.RuntimeException

internal class MonsterPrototypeTest {

    companion object {
        private val NAME = "name"
        private val ID = 1
        // todo load from settings
        private val MAX_HEALTH = 10
        private val MIN_HEALTH = 1
        private val MAX_ATTACK = 10
        private val MIN_ATTACK = 1
        private val MAX_NAME_LENGTH = 9
        private val MIN_NAME_LENGTH = 1
    }

    @Test
    internal fun testConstructor() {
        val monsterPrototype = MonsterPrototype(ID, NAME, MIN_HEALTH, MIN_ATTACK)

        assertEquals(NAME, monsterPrototype.name)
        assertEquals(ID, monsterPrototype.id)
        assertEquals(MIN_HEALTH, monsterPrototype.baseHealth)
        assertEquals(MIN_HEALTH, monsterPrototype.baseAttack)
    }

    @Test
    internal fun testEquals() {
        val monsterOne = MonsterPrototype(ID, "potato one", MAX_HEALTH, MAX_ATTACK)

        val monsterTwo = MonsterPrototype(ID, "potato two", MAX_HEALTH, MAX_ATTACK)

        assertEquals(monsterOne, monsterTwo)
        assertEquals(monsterTwo, monsterOne)
    }

    @Test
    internal fun testNotEquals() {
        val monsterOne = MonsterPrototype(1, NAME, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(2, NAME, MAX_HEALTH, MAX_ATTACK)

        assertNotEquals(monsterOne, monsterTwo)
        assertNotEquals(monsterTwo, monsterOne)
    }

    @Test
    internal fun testCreateMonsterWithTooHighBaseHealth() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, NAME, MAX_HEALTH + 1, MAX_ATTACK)
    })

    @Test
    internal fun testCreateMonsterWithTooHighAttack() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, NAME, MAX_HEALTH, MAX_ATTACK + 1)
    })

    @Test
    internal fun testCreateMonsterWithTooLowAttack() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, NAME, MAX_HEALTH, MIN_ATTACK - 1)
    })

    @Test
    internal fun testCreateMonsterWithTooLowBaseHealth() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, NAME, MIN_HEALTH - 1, MIN_ATTACK)
    })

    @Test
    internal fun testCreateMonsterWithTooShortName() = shouldThrowRuntimeException(Executable {
        val stringBuilder = StringBuilder()
        repeat(MIN_NAME_LENGTH - 1) { stringBuilder.append("a") }
        val name = stringBuilder.toString()

        MonsterPrototype(ID, name, MAX_HEALTH, MAX_ATTACK)
    })

    @Test
    internal fun testCreateMonsterWithTooLongName() = shouldThrowRuntimeException(Executable {
        val stringBuilder = StringBuilder()
        repeat(MAX_NAME_LENGTH + 1) { stringBuilder.append("a") }.toString()
        val name = stringBuilder.toString()

        MonsterPrototype(ID, name, MAX_HEALTH, MAX_ATTACK)
    })

    @Test
    internal fun testCreateMonsterWithInvalidName() = shouldThrowRuntimeException(Executable {
        MonsterPrototype(ID, " 2#_?!<.)", MAX_HEALTH, MAX_ATTACK)
    })

    // todo test equals with other CardPrototype classes

    private fun shouldThrowRuntimeException(executable: Executable) {
        assertThrows(RuntimeException::class.java, executable)
        return Unit
    }
}