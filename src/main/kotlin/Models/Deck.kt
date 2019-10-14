package Models

import Card
import Monster

class Deck(
    empty: Boolean = true,
    cards: ArrayList<Card> = ArrayList()
) : CardList(empty, cards) {
    val maxSize: Int = Settings.DECK_SIZE

    fun shuffleDeck() {}

    fun drawCard(): Card {
        return Monster()
    }
}
