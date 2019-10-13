package prototype

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.nio.file.Path

internal class DeckPrototypeTest {

    companion object {
        val ob = ObjectMapper()

        // todo load info from config

        private const val MAX_HEALTH = 10
        private const val MAX_ATTACK = 10
        private const val MAX_NAME_LENGTH = 9
        private const val MAX_DECK_NAME_LENGTH = 20
        private const val MIN_DECK_NAME_LENGTH = 1
        private const val ID_ONE = 1
        private const val ID_TWO = 2
        private const val ID_THREE = 3
        private val type =
            ObjectMapper().typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)
        private val cardsPath = Path.of("json", "cards.json").toAbsolutePath().toString()
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
        assertEquals(0, deckPrototype.size)
    }

    @Test
    internal fun `Constructor two goes right`() {
        // todo implement
    }

    @Test
    internal fun `Constructor one invalid name`() = shouldThrowRuntimeException(Executable {
        DeckPrototype("£#__.!23?{%&<>")
    })

    @Test
    internal fun `Constructor two invalid name`() {
        // todo implement
    }

    @Test
    internal fun `Constructor one name too short`() {

    }

    @Test
    internal fun `Constructor two name too short`() {

    }

    @Test
    internal fun `Constructor one name too long`() {

    }

    @Test
    internal fun `Constructor two name too long`() {

    }

    @Test
    internal fun `Constructor two jsonDeck too short`() {

    }

    @Test
    internal fun `Add card goes right`() {

    }

    @Test
    internal fun `Add card too many cards`() {

    }

    @Test
    internal fun `Remove card goes right`() {

    }

    @Test
    internal fun `Remove card id do not exist`() {

    }

    @Test
    internal fun `Remove card by id goes right`() {

    }

    @Test
    internal fun `Remove card by id, id do not exists`() {

    }

    @Test
    internal fun `cardList test`() {

    }

    @Test
    internal fun `toString test`() {

    }

    private fun shouldThrowRuntimeException(executable: Executable) {
        Assertions.assertThrows(RuntimeException::class.java, executable)
        return Unit
    }
}