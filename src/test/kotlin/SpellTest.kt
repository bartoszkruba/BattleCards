import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SpellTest {

    companion object {
        const val MAX_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        const val MIN_NAME_LENGTH = Settings.MIN_CARD_NAME_LENGTH
    }

    val NAME: String

    init {
        val sbA = StringBuilder()
        repeat(MAX_NAME_LENGTH) {
            sbA.append("A")
        }

        NAME = sbA.toString()
    }

    @Test
    internal fun `Constructor test`() {
        val spell = Spell(NAME)

        assertEquals(NAME, spell.name)
        assertEquals(CardType.SPEll, spell.type)
    }

    @Test
    internal fun `Creating spell with too long name throws exception`() {
        val sb = StringBuilder()

        repeat(MAX_NAME_LENGTH + 1) { sb.append("a") }
        val name = sb.toString()

        assertThrows<RuntimeException> {
            Spell(name)
        }
    }

    @Test
    internal fun `Creating spell with too short name throws exception`() {
        val sb = StringBuilder()

        repeat(MIN_NAME_LENGTH - 1) { sb.append("a") }
        val name = sb.toString()

        assertThrows<RuntimeException> { Spell(name) }
    }

    @Test
    internal fun `Creating spell with invalid name throws exception`() {
        val name = "#!_<%?1^,"

        assertThrows<RuntimeException> { Spell(name) }
    }
}