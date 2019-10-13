package prototype

import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.file.Path

class CardLoader(
    private val objectMapper: ObjectMapper = ObjectMapper(),
    private val fileWriter: PrototypeFileWriter = PrototypeFileWriter()
) {

    init {
        objectMapper.enableDefaultTyping()
    }

    private val cardsPath = Path.of("json", "cards.json").toAbsolutePath().toString()
    private val listType =
        ObjectMapper().typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)


    fun saveCard(card: CardPrototype) {
        val cards = loadCardsFile()
        if (cards.contains(card)) throw RuntimeException("Card already exists!") else cards.add(card)
        saveCardsFile(cards)
    }


    fun saveCards(cards: Collection<CardPrototype>) {
        val loaded = loadCardsFile()
        cards.forEach { if (loaded.contains(it)) throw throw RuntimeException("Card already exists!") else loaded.add(it) }
        saveCardsFile(loaded)
    }

    fun loadCards(): ArrayList<CardPrototype> = loadCardsFile()

    fun deleteCard(id: Int) {
        val loaded = loadCards()
        loaded.find { it.id == id }?.let {
            loaded.remove(it)
            saveCardsFile(loaded)
        } ?: kotlin.run { throw RuntimeException("Card do not exist!") }
    }

    fun deleteCards(ids: Collection<Int>) = saveCardsFile(loadCardsFile().filter { !ids.contains(it.id) })

    fun saveDeck(deckName: String, deck: DeckProtoype) {

    }

    fun loadDeck(): DeckProtoype {
        return DeckProtoype()
    }

    private fun loadCardsFile(): ArrayList<CardPrototype> {
        val file = when (val file = try {
            fileWriter.readFile(cardsPath)
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }) {
            null -> "[]"
            else -> file
        }
        return objectMapper.readValue<ArrayList<CardPrototype>>(file, listType)
    }

    private fun saveCardsFile(cards: Collection<CardPrototype>) = try {
        fileWriter.writeFile(cardsPath, objectMapper.writeValueAsString(cards))
    } catch (ex: Exception) {
        throw RuntimeException(ex.message)
    }
}
