package Models

import Card

class Hand(
    empty: Boolean = true,
    cards: ArrayList<Card> = ArrayList()
) : CardList(empty, cards) {

//    var maxSize: Int = Settings.HAND_SIZE

}