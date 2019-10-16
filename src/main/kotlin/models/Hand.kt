package models

import Card

class Hand(
    empty: Boolean = true,
    cards: ArrayList<Card> = ArrayList()
) : CardList(empty, cards,Settings.HAND_SIZE) {}