package models

import Card

class Deck(
    cards: ArrayList<Card> = ArrayList()
) : CardList(cards.size == 0, cards, Settings.DECK_SIZE) {

    fun shuffleDeck() {
        if (size() == 1) return
        val cardsBefore = ArrayList(cards)
        cards.shuffle()
        val cardsAfter = ArrayList(cards)
        if (listsExactlySame(cardsBefore, cardsAfter)) shuffleDeck()
    }

    private fun listsExactlySame(listOne: List<Card>, listTwo: List<Card>): Boolean {
        for (i in listOne.indices) if (listOne[i] != listTwo[i]) return false
        return true
    }

    fun drawCard(): Card? = when (cards.size) {
        0 -> null
        else -> {
            if (cards.size - 1 == 0) empty = true
            cards.removeAt(0)
        }
    }

}
