package models

import Card

class Deck(
    cards: ArrayList<Card> = ArrayList()
) : CardList(cards.size == 0, cards, Settings.DECK_SIZE) {

    fun shuffleDeck() = cards.shuffle()

    fun drawCard(): Card? = when (cards.size) {
        0 -> null
        else -> {
            if (cards.size - 1 == 0) empty = true
            cards.removeAt(0)
        }
    }

}
