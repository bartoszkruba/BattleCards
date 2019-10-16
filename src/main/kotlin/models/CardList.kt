package models

import Card
import utilities.Utils

abstract class CardList(var empty: Boolean, cards: ArrayList<Card>, var maxSize: Int) {
    private var cards: ArrayList<Card> = ArrayList()

    init {
        require(cards.size <= maxSize) { "Card list is too big, ohh nooo!" }
        this.cards = cards.map { Utils.clone(it) as Card } as ArrayList<Card>
    }

    fun cardsInList(): ArrayList<Card> {
        return cards.map { Utils.clone(it) as Card } as ArrayList<Card>
    }

    fun addCard(card: Card): Boolean {
        if (cards.size < maxSize) {
            for (c in cards) {
                if (c.cardId.equals(card.cardId)) {
                    return false
                }
            }

            val copied: Card = Utils.clone(card) as Card
            empty = false
            return cards.add(copied)
        }
        return false
    }

    fun removeCard(card: Card): Card {
        val cardToRemove = cards.findLast { it.cardId == card.cardId }
        val indexToRemoveFrom = cards.indexOf(cardToRemove)
        if (indexToRemoveFrom != -1) {
            val removedCard: Card = cards.removeAt(indexToRemoveFrom);
            if (cards.size == 0) empty = true
            return removedCard
        }
        throw RuntimeException("Card cannot be removed, it doesn't exist")
    }

    override fun toString(): String {
        var lines: ArrayList<String> = arrayListOf("", "", "", "", "")

        for (card in cards) {
            for ((i, line) in card.toString().split("\n").withIndex()) {
                lines[i] += line
            }
        }
        return lines.joinToString("\n")
    }
}