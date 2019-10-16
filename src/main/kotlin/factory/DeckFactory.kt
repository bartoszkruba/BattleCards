package factory

import models.Deck
import prototype.DeckPrototype

class DeckFactory {
    companion object {
        fun createDeck(deck: DeckPrototype): Deck {
            val newDeck = Deck()
            for (entry in deck.cardsCopy().entries) repeat(entry.value) {
                newDeck.addCard(CardFactory.createCard(entry.key))
            }
            return newDeck
        }
    }

}