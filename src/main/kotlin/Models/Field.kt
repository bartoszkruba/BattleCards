package Models

import Card

class Field(
    empty: Boolean = true,
    cards: ArrayList<Card> = ArrayList()
):CardList(empty,cards) {
    val maxSize: Int = Settings.FIELD_SIZE
}