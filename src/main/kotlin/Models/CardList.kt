package Models

import Card
import Monster
import utilities.Utils

abstract class CardList(var empty: Boolean, cards: ArrayList<Card>) {
    var cards: ArrayList<Card> = ArrayList()

    init {
        this.cards = ArrayList(cards)
    }

    fun cardsInList(): ArrayList<Card> {
        return cards
    }

    fun addCard(card: Card): Boolean {
        for(c in cards) {
            if(c.cardId.equals(card.cardId)) {
                return false
            }
        }

        val copied: Card = Utils.clone(card) as Card
        return cards.add(copied)
    }

    fun removeCard(card: Card): Card {
        val indexToRemoveFrom = cards.indexOf(card)
        if (indexToRemoveFrom != -1) {
            val removedCard: Card = cards.removeAt(indexToRemoveFrom);
            if (cards.size == 0) empty = true
            return removedCard
        }
        throw RuntimeException("Card cannot be removed, it doesn't exist")
    }
}