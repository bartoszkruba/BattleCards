package prototype

import com.fasterxml.jackson.databind.ObjectMapper

class CardLoader(
    val objectMapper: ObjectMapper = ObjectMapper(),
    val fileWriter: PrototypeFileWriter = PrototypeFileWriter()
) {

    fun saveCard(card: CardPrototype): Boolean {

        return false
    }

    fun saveCards(cards: Collection<CardPrototype>): Boolean {
        return false
    }

    fun loadCards(): ArrayList<CardPrototype> {

        return ArrayList()
    }

    fun saveDeck(deckName: String, deck: DeckProtoype): Boolean {

        return false
    }

    fun loadDeck(): DeckProtoype {
        return DeckProtoype()
    }
}