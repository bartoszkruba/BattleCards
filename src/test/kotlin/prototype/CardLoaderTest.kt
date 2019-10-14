package prototype

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import java.nio.file.Path

internal class CardLoaderTest {

    companion object {
        val ob = ObjectMapper()

        private const val MAX_HEALTH = Settings.MAX_HEALTH
        private const val MAX_ATTACK = Settings.MAX_DAMAGE
        private const val MAX_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        private const val ID_ONE = 1
        private const val ID_TWO = 2
        private const val ID_THREE = 3
        private val type =
            ObjectMapper().typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)
        private val cardsPath = Path.of("json", "cards.json").toAbsolutePath().toString()
    }

    private val NAME_ONE: String
    private val NAME_TWO: String
    private val NAME_THREE: String

    @Mock
    lateinit var objectMapper: ObjectMapper

    @Mock
    lateinit var fileWriter: PrototypeFileWriter

    lateinit var cardLoader: CardLoader

    init {
        val stringBuilderA = StringBuilder()
        val stringBuilderB = StringBuilder()
        val stringBuilderC = StringBuilder()

        repeat(MAX_NAME_LENGTH) {
            stringBuilderA.append("a")
            stringBuilderB.append("b")
            stringBuilderC.append("c")
        }

        NAME_ONE = stringBuilderA.toString()
        NAME_TWO = stringBuilderB.toString()
        NAME_THREE = stringBuilderC.toString()
    }


    @BeforeEach
    fun setUp() {
        objectMapper = Mockito.mock(ObjectMapper::class.java)
        fileWriter = Mockito.mock(PrototypeFileWriter::class.java)

        cardLoader = CardLoader(objectMapper, fileWriter)

        Mockito.verify(objectMapper, times(1)).enableDefaultTyping()
    }

    @Test
    fun `Saving MonsterPrototype goes right`() {

        // Preparation

        val monsterPrototype = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val initialCardList = ArrayList<CardPrototype>()
        val newCardList = ArrayList(listOf<CardPrototype>(monsterPrototype))

        val initialJSON = ob.writeValueAsString(initialCardList)
        val newJSON = ob.writeValueAsString(newCardList)

        given(fileWriter.readFile(cardsPath)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialCardList)

        given(objectMapper.writeValueAsString(newCardList)).willReturn(newJSON)

        // Testing

        cardLoader.saveCard(monsterPrototype)

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verify(objectMapper, times(1)).writeValueAsString(newCardList)
        verify(fileWriter, times(1)).writeFile(cardsPath, newJSON)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving MonsterProtototype with existing id`() {

        // Preparation

        val monsterPrototype = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)

        val initialList = ArrayList(listOf<CardPrototype>(monsterPrototype))
        val initialJSON = ob.writeValueAsString(initialList)

        given(fileWriter.readFile(cardsPath)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialList)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.saveCard(monsterPrototype) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving prototype filewriter throws exception`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)

        given(fileWriter.readFile(cardsPath)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.saveCard(monsterOne) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving prototype filewriter throws exception during saving`() {
        val monsterPrototype = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val initialCardList = ArrayList<CardPrototype>()
        val newCardList = ArrayList(listOf<CardPrototype>(monsterPrototype))

        val initialJSON = ob.writeValueAsString(initialCardList)
        val newJSON = ob.writeValueAsString(newCardList)

        given(fileWriter.readFile(cardsPath)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialCardList)
        given(objectMapper.writeValueAsString(newCardList)).willReturn(newJSON)
        given(fileWriter.writeFile(cardsPath, newJSON)).willThrow(RuntimeException::class.java)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.saveCard(monsterPrototype) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verify(objectMapper, times(1)).writeValueAsString(newCardList)
        verify(fileWriter, times(1)).writeFile(cardsPath, newJSON)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    fun `Saving list of cards goes right`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val initialList = ArrayList<CardPrototype>()
        val newList = ArrayList(listOf(monsterOne, monsterTwo))

        val initialJSON = ob.writeValueAsString(initialList)
        val newJSON = ob.writeValueAsString(newList)

        given(fileWriter.readFile(cardsPath)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialList)

        given(objectMapper.writeValueAsString(newList)).willReturn(newJSON)

        cardLoader.saveCards(newList)

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verify(objectMapper, times(1)).writeValueAsString(newList)
        verify(fileWriter, times(1)).writeFile(cardsPath, newJSON)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    fun `Saving list of cards with existing id`() {

        // Preparation

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_ONE, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val initialList = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo))
        val cardsToAdd = ArrayList<CardPrototype>(listOf(monsterTwo, monsterThree))
        val initialJSON = ob.writeValueAsString(initialList)

        given(fileWriter.readFile(cardsPath)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialList)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.saveCards(cardsToAdd) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving cards filewriter throws exception`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val list = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo))

        given(fileWriter.readFile(cardsPath)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.saveCards(list) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving cards filewriter throws exception during saving`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val initialList = ArrayList<CardPrototype>()
        val newList = ArrayList(listOf(monsterOne, monsterTwo))

        val initialJSON = ob.writeValueAsString(initialList)
        val newJSON = ob.writeValueAsString(newList)

        given(fileWriter.readFile(cardsPath)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialList)
        given(objectMapper.writeValueAsString(newList)).willReturn(newJSON)
        given(fileWriter.writeFile(cardsPath, newJSON)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.saveCards(newList) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verify(objectMapper, times(1)).writeValueAsString(newList)
        verify(fileWriter, times(1)).writeFile(cardsPath, newJSON)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    fun `Loading cards goes right`() {

        // Preparation

        val testString = "test"

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val list = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo, monsterThree))

        // Testing

        given(fileWriter.readFile(cardsPath)).willReturn(testString)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testString, type)).willReturn(list)

        val returnedList = cardLoader.loadCards()

        assertEquals(3, returnedList.size)
        assertEquals(list.size, 3)
        assertTrue(list.containsAll(listOf(monsterOne, monsterTwo, monsterThree)))

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testString, type)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Loading cards filewriter throws exception`() {
        given(fileWriter.readFile(cardsPath)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.loadCards() })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Delete card goes right`() {

        // Preparation

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val listBefore = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo, monsterThree))
        val listAfter = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo))

        val testStringOne = "testOne"
        val testStringTwo = "testTwo"

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(listBefore)
        given(objectMapper.writeValueAsString(listAfter)).willReturn(testStringTwo)

        // Testing

        cardLoader.deleteCard(ID_THREE)

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(listAfter)
        verify(fileWriter, times(1)).writeFile(cardsPath, testStringTwo)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Deleting card with invalid id`() {

        // Preparation

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val list = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo))

        val testString = "test"

        given(fileWriter.readFile(cardsPath)).willReturn(testString)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testString, type)).willReturn(list)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.deleteCard(ID_THREE) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testString, type)
    }

    @Test
    internal fun `Deleting card filewriter throws exception`() {

        // Preparation

        given(fileWriter.readFile(cardsPath)).willThrow(RuntimeException::class.java)

        // Testing
        shouldThrowRuntimeException(Executable { cardLoader.deleteCard(ID_ONE) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Deleting card filewriter throws exception during saving`() {
        // Preparation

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val listBefore = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo, monsterThree))
        val listAfter = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo))

        val testStringOne = "testOne"
        val testStringTwo = "testTwo"

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(listBefore)
        given(objectMapper.writeValueAsString(listAfter)).willReturn(testStringTwo)
        given(fileWriter.writeFile(cardsPath, testStringTwo)).willThrow(RuntimeException::class.java)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.deleteCard(ID_THREE) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(listAfter)
        verify(fileWriter, times(1)).writeFile(cardsPath, testStringTwo)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Deleting cards goes right`() {

        // Preparation

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val testStringOne = "test one"
        val testStringTwo = "test two"

        val initialList = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo, monsterThree))
        val newList = ArrayList<CardPrototype>(listOf(monsterThree))

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(initialList)
        given(objectMapper.writeValueAsString(newList)).willReturn(testStringTwo)

        // Testing

        cardLoader.deleteCards(listOf(ID_ONE, ID_TWO, 123))

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(newList)
        verify(fileWriter, times(1)).writeFile(cardsPath, testStringTwo)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Deleting cards filewriter throws exception`() {
        given(fileWriter.readFile(cardsPath)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.deleteCards(listOf(1, 2, 3)) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Deleting cards filewriter throws exception during saving`() {

        // Preparation

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val testStringOne = "test one"
        val testStringTwo = "test two"

        val initialList = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo, monsterThree))
        val newList = ArrayList<CardPrototype>(listOf(monsterThree))

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(initialList)
        given(objectMapper.writeValueAsString(newList)).willReturn(testStringTwo)
        given(fileWriter.writeFile(cardsPath, testStringTwo)).willThrow(RuntimeException::class.java)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.deleteCards(listOf(ID_ONE, ID_TWO, 123)) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(newList)
        verify(fileWriter, times(1)).writeFile(cardsPath, testStringTwo)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Deleting cards no matching id`() {

        // Preparation

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val testStringOne = "test one"
        val testStringTwo = "test two"

        val initialList = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo, monsterThree))
        val newList = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo, monsterThree))

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(initialList)
        given(objectMapper.writeValueAsString(newList)).willReturn(testStringTwo)

        // Testing

        cardLoader.deleteCards(listOf(123, 12312, 1321))

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(newList)
        verify(fileWriter, times(1)).writeFile(cardsPath, testStringTwo)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    fun saveDeck() {
    }

    @Test
    fun loadDeck() {
    }

    private fun shouldThrowRuntimeException(executable: Executable) {
        assertThrows(RuntimeException::class.java, executable)
        return Unit
    }

}