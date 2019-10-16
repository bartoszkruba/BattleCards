package models

import Card

class Field(
    empty: Boolean = true,
    cards: ArrayList<Card> = ArrayList()
):CardList(empty,cards,Settings.FIELD_SIZE) {}