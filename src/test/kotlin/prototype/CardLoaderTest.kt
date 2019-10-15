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

        private const val MAX_DECK_SIZE = Settings.DECK_SIZE
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
    private val DECK_NAME_ONE: String

    @Mock
    lateinit var objectMapper: ObjectMapper

    @Mock
    lateinit var fileWriter: PrototypeFileWriter

    lateinit var cardLoader: CardLoader

    init {
        val stringBuilderA = StringBuilder()
        val stringBuilderB = StringBuilder()
        val stringBuilderC = StringBuilder()
        val stringBuilderD = StringBuilder()

        repeat(MAX_NAME_LENGTH) {
            stringBuilderA.append("a")
            stringBuilderB.append("b")
            stringBuilderC.append("c")
        }

        repeat(MAX_DECK_SIZE) {
            stringBuilderD.append("d")
        }

        NAME_ONE = stringBuilderA.toString()
        NAME_TWO = stringBuilderB.toString()
        NAME_THREE = stringBuilderC.toString()
        DECK_NAME_ONE = stringBuilderD.toString()
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
    fun `loadDeck() goes right`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val deckName = "name"

        val path = Path.of("json", "decks", "$deckName.json").toAbsolutePath().toString()

        val deckPrototype = DeckPrototype(NAME_ONE)
        deckPrototype.addCard(monsterOne)
        deckPrototype.addCard(monsterTwo)
        deckPrototype.addCard(monsterThree)
        val jsonDeck = JsonDeck(deckPrototype)
        val cards = arrayListOf<CardPrototype>(monsterOne, monsterTwo, monsterThree)

        val testStringOne = "test one"
        val testStringTwo = "test two"

        given(fileWriter.readFile(cardsPath)).willReturn(testStringTwo)
        given(fileWriter.readFile(path)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringTwo, type)).willReturn(cards)
        given(objectMapper.readValue<JsonDeck>(testStringOne, JsonDeck::class.java)).willReturn(jsonDeck)

        val loadedDeckPrototype = cardLoader.loadDeck(deckName)
        assertEquals(3, deckPrototype.size)
        assertEquals(deckName, loadedDeckPrototype.name)
        val cardsCopy = deckPrototype.cardsCopy()
        assertEquals(3, cardsCopy.size)
        assertEquals(1, cardsCopy[monsterOne])
        assertEquals(1, cardsCopy[monsterTwo])
        assertEquals(1, cardsCopy[monsterThree])

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringTwo, type)
        verify(objectMapper, times(1)).readValue<JsonDeck>(testStringOne, JsonDeck::class.java)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `loadDeck(), wilewriter loadFile returns null`() {
        given(fileWriter.readFile(cardsPath)).willReturn(null)

        shouldThrowRuntimeException(Executable { cardLoader.loadDeck("dd") })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `loadDeck(), deck ids do not match with loaded cards`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val deckName = "name"

        val path = Path.of("json", "decks", "$deckName.json").toAbsolutePath().toString()

        val deckPrototype = DeckPrototype(NAME_ONE)
        deckPrototype.addCard(monsterOne)
        deckPrototype.addCard(monsterTwo)
        deckPrototype.addCard(monsterThree)
        val jsonDeck = JsonDeck(deckPrototype)
        val cards = arrayListOf<CardPrototype>(monsterOne, monsterTwo)

        val testStringOne = "test one"
        val testStringTwo = "test two"

        given(fileWriter.readFile(cardsPath)).willReturn(testStringTwo)
        given(fileWriter.readFile(path)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringTwo, type)).willReturn(cards)
        given(objectMapper.readValue<JsonDeck>(testStringOne, JsonDeck::class.java)).willReturn(jsonDeck)

        shouldThrowRuntimeException(Executable { cardLoader.loadDeck(deckName) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(fileWriter, times(1)).readFile(path)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringTwo, type)
        verify(objectMapper, times(1)).readValue<JsonDeck>(testStringOne, JsonDeck::class.java)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `loadDeck(), deck do not exist`() {
        val testStringOne = "test one"
        val deckName = "name"

        val deckPath = Path.of("json", "decks", "$deckName.json").toString()

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(fileWriter.readFile(deckPath)).willReturn(null)

        shouldThrowRuntimeException(Executable { cardLoader.loadDeck(deckName) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(fileWriter, times(1)).readFile(deckPath)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `loadDeck(), filewriter throws exception during loading cards`() {
        given(fileWriter.readFile(cardsPath)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.loadDeck("dd") })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `loadDeck(), filewriter throws exception during loading deck`() {
        val testStringOne = "test one"

        val deckPath = Path.of("json", "decks", "$DECK_NAME_ONE.json").toAbsolutePath().toString()

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(fileWriter.readFile(deckPath)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.loadDeck(DECK_NAME_ONE) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(fileWriter, times(1)).readFile(deckPath)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `saveDeck() goes right`() {
        val testStringOne = "test one"
        val testStringTwo = "test two"

        val deckPath = Path.of("json", "decks", "$DECK_NAME_ONE.json").toAbsolutePath().toString()

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val cards = arrayListOf<CardPrototype>(monsterOne, monsterTwo, monsterThree)
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        repeat(MAX_DECK_SIZE) { deckPrototype.addCard(monsterOne) }
        val jsonDeck = JsonDeck(deckPrototype)

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(cards)
        given(objectMapper.writeValueAsString(jsonDeck)).willReturn(testStringTwo)

        cardLoader.saveDeck(deckPrototype)

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(jsonDeck)
        verify(fileWriter, times(1)).writeFile(deckPath, testStringTwo)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `saveDeck(), invalid deck length`() {
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)

        shouldThrowRuntimeException(Executable { cardLoader.saveDeck(deckPrototype) })
    }

    @Test
    internal fun `saveDeck(), ids do not match with loaded cards`() {
        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val cards = arrayListOf<CardPrototype>(monsterOne, monsterThree)

        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        repeat(MAX_DECK_SIZE - 2) { deckPrototype.addCard(monsterOne) }
        deckPrototype.addCard(monsterTwo)
        deckPrototype.addCard(monsterThree)

        val testString = "test one"

        given(fileWriter.readFile(cardsPath)).willReturn(testString)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testString, type)).willReturn(cards)

        shouldThrowRuntimeException(Executable { cardLoader.saveDeck(deckPrototype) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testString, type)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `loadDeck, filewrtier throws exception during loading cards`() {
        given(fileWriter.readFile(cardsPath)).willThrow(RuntimeException::class.java)

        verify(fileWriter, times(1)).readFile(cardsPath)

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)

        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        repeat(MAX_DECK_SIZE) { deckPrototype.addCard(monsterOne) }

        shouldThrowRuntimeException(Executable { cardLoader.saveDeck(deckPrototype) })

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    @Test
    internal fun `loadDeck(), filewriter throws exception during saving deck`() {
        val testStringOne = "test one"
        val testStringTwo = "test two"

        val deckPath = Path.of("json", "decks", "$DECK_NAME_ONE.json").toAbsolutePath().toString()

        val monsterOne = MonsterPrototype(ID_ONE, NAME_ONE, MAX_HEALTH, MAX_ATTACK)
        val monsterTwo = MonsterPrototype(ID_TWO, NAME_TWO, MAX_HEALTH, MAX_ATTACK)
        val monsterThree = MonsterPrototype(ID_THREE, NAME_THREE, MAX_HEALTH, MAX_ATTACK)

        val cards = arrayListOf<CardPrototype>(monsterOne, monsterTwo, monsterThree)
        val deckPrototype = DeckPrototype(DECK_NAME_ONE)
        repeat(MAX_DECK_SIZE) { deckPrototype.addCard(monsterOne) }
        val jsonDeck = JsonDeck(deckPrototype)

        given(fileWriter.readFile(cardsPath)).willReturn(testStringOne)
        given(objectMapper.readValue<ArrayList<CardPrototype>>(testStringOne, type)).willReturn(cards)
        given(objectMapper.writeValueAsString(jsonDeck)).willReturn(testStringTwo)
        given(fileWriter.writeFile(deckPath, testStringTwo)).willThrow(RuntimeException::class.java)

        shouldThrowRuntimeException(Executable { cardLoader.saveDeck(deckPrototype) })

        verify(fileWriter, times(1)).readFile(cardsPath)
        verify(objectMapper, times(1)).readValue<ArrayList<CardPrototype>>(testStringOne, type)
        verify(objectMapper, times(1)).writeValueAsString(jsonDeck)
        verify(fileWriter, times(1)).writeFile(deckPath, testStringTwo)

        verifyNoMoreInteractions(fileWriter)
        verifyNoMoreInteractions(objectMapper)
    }

    private fun shouldThrowRuntimeException(executable: Executable) {
        assertThrows(RuntimeException::class.java, executable)
        return Unit
    }

}