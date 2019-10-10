package Models

class Hand(
    var empty: Boolean = true,
    var cards: ArrayList<Card> = ArrayList()
) : CardList(empty, cards) {

    var maxSize: Int = Settings.HAND_SIZE

}