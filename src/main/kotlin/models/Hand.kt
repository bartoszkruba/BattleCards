package models

import Card

class Hand(
    cards: ArrayList<Card> = ArrayList()
) : CardList(cards.size == 0, cards, Settings.HAND_SIZE) {}