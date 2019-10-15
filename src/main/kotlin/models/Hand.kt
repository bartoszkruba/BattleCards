package models

import Card

class Hand(
    empty: Boolean = true,
    cards: ArrayList<Card> = ArrayList()
) : CardList(empty, cards) {
    val maxSize: Int = Settings.HAND_SIZE
}