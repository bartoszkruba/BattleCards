package models

import Card
import Monster

class Deck(
    empty: Boolean = true,
    cards: ArrayList<Card> = ArrayList()
) : CardList(empty, cards,Settings.DECK_SIZE) {
    fun shuffleDeck() {}

    fun drawCard(): Card {
        return Monster()
    }

}
