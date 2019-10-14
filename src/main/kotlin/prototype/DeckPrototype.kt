package prototype

class DeckPrototype(val name: String) {

    companion object {
        private const val MIN_NAME_LENGTH = 1
        private const val MAX_NAME_LENGTH = 20
        private val NAME_REGEX = Regex("")
    }

    val cards = HashMap<CardPrototype, Int>()
        get() {
            return field
        }

    var size = 0
        private set

    fun addCard(card: CardPrototype): Boolean {
        return false
    }

    fun removeCard(card: CardPrototype): Boolean {
        return false
    }

    fun removeCard(id: Int): Boolean {
        return false
    }

    override fun toString(): String {
        return ""
    }
}