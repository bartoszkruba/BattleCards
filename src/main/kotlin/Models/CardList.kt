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
        val indexToRemoveFrom = cards.indexOf(card)
        if(indexToRemoveFrom != -1){
            val removedCard:Card = cards.removeAt(indexToRemoveFrom);
            if(cards.size == 0) empty = true
            return removedCard
        }else{
            throw RuntimeException()
        }
    }
}