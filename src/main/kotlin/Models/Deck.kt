package Models

import Card

class Deck(
    empty: Boolean = true,
    cards: ArrayList<Card> = ArrayList()
) : CardList(empty, cards) {
    val maxSize: Int = 30

    fun shuffleDeck() {}

    fun drawCard(): Card {
        return Card()
    }
}
