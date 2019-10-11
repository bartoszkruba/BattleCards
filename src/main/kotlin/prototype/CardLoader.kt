package prototype

import com.fasterxml.jackson.databind.ObjectMapper

class CardLoader(
    private val objectMapper: ObjectMapper = ObjectMapper(),
    private val fileWriter: PrototypeFileWriter = PrototypeFileWriter()
) {

    fun deleteCard(id: Int) {

    }

    fun deleteCards(ids: Collection<Int>) {

    }

    fun saveCard(card: CardPrototype){

    }

    fun saveCards(cards: Collection<CardPrototype>){
    }

    fun loadCards(): ArrayList<CardPrototype> {

        return ArrayList()
    }

    fun saveDeck(deckName: String, deck: DeckProtoype){

    }

    fun loadDeck(): DeckProtoype {
        return DeckProtoype()
    }
}