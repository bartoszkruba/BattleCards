package models

import Card
import utilities.Utils

abstract class CardList(var empty: Boolean, cards: ArrayList<Card>) {
    private var cards: ArrayList<Card> = ArrayList()

    init {
        this.cards = cards.map{Utils.clone(it) as Card} as ArrayList<Card>
    }

    fun cardsInList(): ArrayList<Card> {
        return cards.map{Utils.clone(it) as Card} as ArrayList<Card>
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
        val cardToRemove = cards.findLast{it.cardId == card.cardId}
        val indexToRemoveFrom = cards.indexOf(cardToRemove)
        if (indexToRemoveFrom != -1) {
            val removedCard: Card = cards.removeAt(indexToRemoveFrom);
            if (cards.size == 0) empty = true
            return removedCard
        }
        throw RuntimeException("Card cannot be removed, it doesn't exist")
    }
}