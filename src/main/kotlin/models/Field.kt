package models

import Card

class Field(
    cards: ArrayList<Card> = ArrayList()
) : CardList(cards.size == 0, cards, Settings.FIELD_SIZE) {}