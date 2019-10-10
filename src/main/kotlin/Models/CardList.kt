package Models

import Card

abstract class CardList(var empty: Boolean = true,
                        var cards: ArrayList<Card> = ArrayList()) {

    fun cardsInList(): ArrayList<Card> {
        return cards
    }

    fun addCard(card: Card): Boolean {
        return false
    }

    fun removeCard(card: Card): Card {
        return card
    }
}