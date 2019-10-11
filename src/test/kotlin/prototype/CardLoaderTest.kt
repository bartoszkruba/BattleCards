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

        // todo load info from config

        private const val MAX_HEALTH = 10
        private const val MAX_ATTACK = 10
        private const val MAX_NAME_LENGTH = 9
        private const val ID_ONE = 1
        private const val ID_TWO = 2
        private const val ID_THREE = 3
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
    }

    @Test
    fun `Saving MonsterPrototype goes right`() {

        // Preparation

        val monsterPrototype = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()
        val initialCardList = ArrayList<CardPrototype>()
        val newCardList = ArrayList(listOf<CardPrototype>(monsterPrototype))

        val initialJSON = ob.writeValueAsString(initialCardList)
        val newJSON = ob.writeValueAsString(newCardList)

        val type = ob.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)

        given(fileWriter.readFile(path)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialCardList)

        given(objectMapper.writeValueAsString(newCardList)).willReturn(newJSON)

        // Testing

        cardLoader.saveCard(monsterPrototype)

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verify(objectMapper, times(1)).writeValueAsString(newJSON)
        verify(fileWriter, times(1)).writeFile(path, newJSON)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving MonsterProtototype with existing id`() {

        // Preparation

        val monsterPrototype = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        val initialList = ArrayList(listOf<CardPrototype>(monsterPrototype))
        val initialJSON = ob.writeValueAsString(initialList)

        val type = ob.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)

        given(fileWriter.readFile(path)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialList)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.saveCard(monsterPrototype) })

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving prototype filewriter throws exception`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)

        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        given(fileWriter.readFile(path)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.saveCard(monsterOne) })

        verify(fileWriter, times(1)).readFile(path)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving prototype filewriter throws exception during saving`() {
        val monsterPrototype = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()
        val initialCardList = ArrayList<CardPrototype>()
        val newCardList = ArrayList(listOf<CardPrototype>(monsterPrototype))

        val initialJSON = ob.writeValueAsString(initialCardList)
        val newJSON = ob.writeValueAsString(newCardList)

        val type = ob.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)

        given(fileWriter.readFile(path)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialCardList)
        given(objectMapper.writeValueAsString(newCardList)).willReturn(newJSON)
        given(fileWriter.writeFile(path, newJSON)).willThrow(Exception::class.java)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.saveCard(monsterPrototype) })

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verify(objectMapper, times(1)).writeValueAsString(newJSON)
        verify(fileWriter, times(1)).writeFile(path, newJSON)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    fun `Saving list of cards goes right`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val initialList = ArrayList<CardPrototype>()
        val newList = ArrayList(listOf(monsterOne, monsterTwo))

        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        val initialJSON = ob.writeValueAsString(initialList)
        val newJSON = ob.writeValueAsString(newList)

        val type = objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)

        given(fileWriter.readFile(path)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialList)

        given(objectMapper.writeValueAsString(newList)).willReturn(newJSON)

        cardLoader.saveCards(newList)

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verify(objectMapper, times(1)).writeValueAsString(newList)
        verify(fileWriter, times(1)).writeFile(path, newJSON)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    fun `Saving list of cards with existing id`() {

        // Preparation

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_ONE, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val type = objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)

        val initialList = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo))
        val cardsToAdd = ArrayList<CardPrototype>(listOf(monsterTwo, monsterThree))
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()
        val initialJSON = ob.writeValueAsString(initialList)

        given(fileWriter.readFile(path)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialList)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.saveCards(cardsToAdd) })

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving cards filewriter throws exception`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val list = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo))
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        given(fileWriter.readFile(path)).willThrow(Exception::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.saveCards(list) })

        verify(fileWriter, times(1)).readFile(path)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `Saving cards filewriter throws exception during saving`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val initialList = ArrayList<CardPrototype>()
        val newList = ArrayList(listOf(monsterOne, monsterTwo))

        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        val initialJSON = ob.writeValueAsString(initialList)
        val newJSON = ob.writeValueAsString(newList)

        val type = objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)

        given(fileWriter.readFile(path)).willReturn(initialJSON)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(initialJSON, type)).willReturn(initialList)
        given(objectMapper.writeValueAsString(newList)).willReturn(newJSON)
        given(fileWriter.writeFile(path, newJSON)).willThrow(Exception::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.saveCards(newList) })

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(initialJSON, type)

        verify(objectMapper, times(1)).writeValueAsString(newList)
        verify(fileWriter, times(1)).writeFile(path, newJSON)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    fun `Loading cards goes right`() {

        // Preparation

        val testString = "test"
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_TWO, MAX_HEALTH, MAX_ATTACK)

        val list = ArrayList<CardPrototype>(listOf(monsterOne, monsterTwo, monsterThree))
        val type = objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)

        // Testing

        given(fileWriter.readFile(path)).willReturn(testString)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testString, type)).willReturn(list)

        val returnedList = cardLoader.loadCards()

        assertEquals(3, returnedList.size)
        assertEquals(list.size, 3)
        assertTrue(list.containsAll(listOf(monsterOne, monsterTwo, monsterThree)))

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testString, type)
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

        val type = objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        given(fileWriter.readFile(path)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(listBefore)
        given(objectMapper.writeValueAsString(listAfter)).willReturn(testStringTwo)

        // Testing

        cardLoader.deleteCard(ID_THREE)

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(listAfter)
        verify(fileWriter, times(1)).writeFile(path, testStringTwo)
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

        val type = objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        given(fileWriter.readFile(path)).willReturn(testString)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testString, type)).willReturn(list)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.deleteCard(ID_THREE) })

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testString, type)
    }

    @Test
    internal fun `Deleting card filewriter throws exception`() {

        // Preparation

        val testString = "test"

        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        given(fileWriter.readFile(path)).willThrow(Exception::class.java)

        // Testing
        cardLoader.deleteCard(ID_ONE)

        verify(fileWriter, times(1)).readFile(path)
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

        val type = objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)
        val path = Path.of("json", "cards.json").toAbsolutePath().toString()

        given(fileWriter.readFile(path)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(listBefore)
        given(objectMapper.writeValueAsString(listAfter)).willReturn(testStringTwo)
        given(fileWriter.writeFile(path, testStringTwo)).willThrow(Exception::class.java)

        // Testing

        shouldThrowRuntimeException(Executable { cardLoader.deleteCard(ID_THREE) })

        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(listAfter)
        verify(fileWriter, times(1)).writeFile(path, testStringTwo)
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