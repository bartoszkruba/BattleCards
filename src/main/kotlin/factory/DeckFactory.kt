package factory

import models.Deck
import prototype.DeckPrototype

class DeckFactory {
    companion object {
        fun createDeck(deck: DeckPrototype): Deck {
            return Deck()
        }
    }

}