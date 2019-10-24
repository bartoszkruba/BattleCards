package prototype

import CardType

abstract class CardPrototype(open val id: Int, open val name: String, val type: CardType) : Clonable {

    companion object {
        val MAX_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        val MIN_NAME_LENGTH = Settings.MIN_CARD_NAME_LENGTH
        val NAME_REGEX = Regex("[a-zA-z ]*")
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true


        return if (other is CardPrototype) {
            id == other.id
        } else false
    }

    override fun hashCode(): Int {
        return id
    }

}