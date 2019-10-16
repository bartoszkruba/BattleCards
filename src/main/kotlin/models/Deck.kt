package models

import Card

class Deck(
    cards: ArrayList<Card> = ArrayList()
) : CardList(cards.size == 0, cards, Settings.DECK_SIZE) {

    fun shuffleDeck() {
        
    }

    fun drawCard(): Card? {
        return null
    }

}
