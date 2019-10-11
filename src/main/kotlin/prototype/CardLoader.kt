package prototype

import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.file.Path

class CardLoader(
    private val objectMapper: ObjectMapper = ObjectMapper(),
    private val fileWriter: PrototypeFileWriter = PrototypeFileWriter()
) {

    private val cardsPath = Path.of("json", "cards.json").toAbsolutePath().toString()
    private val listType =
        ObjectMapper().typeFactory.constructCollectionType(ArrayList::class.java, CardPrototype::class.java)


    fun saveCard(card: CardPrototype) {

        val jsonFile = when (val file = try {
            fileWriter.readFile(cardsPath)
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }) {
            null -> "[]"
            else -> file
        }

        val cards = objectMapper.readValue<ArrayList<CardPrototype>>(jsonFile, listType)

        if (cards.contains(card)) throw RuntimeException("Card already exists!") else cards.add(card)

        try {
            fileWriter.writeFile(cardsPath, objectMapper.writeValueAsString(cards))
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }
    }

    fun saveCards(cards: Collection<CardPrototype>) {
    }

    fun loadCards(): ArrayList<CardPrototype> {

        return ArrayList()
    }

    fun deleteCard(id: Int) {

    }

    fun deleteCards(ids: Collection<Int>) {

    }

    fun saveDeck(deckName: String, deck: DeckProtoype) {

    }

    fun loadDeck(): DeckProtoype {
        return DeckProtoype()
    }
}
