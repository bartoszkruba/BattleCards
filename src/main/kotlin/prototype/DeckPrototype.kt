package prototype

class DeckPrototype(val name: String) {

    companion object {
        private const val MIN_NAME_LENGTH = Settings.MIN_DECK_NAME_LENGTH
        private const val MAX_NAME_LENGTH = Settings.MAX_DECK_NAME_LENGTH
        private const val MAX_DECK_LENGTH = Settings.DECK_SIZE
        val NAME_REGEX = Regex("[a-zA-z ]*")
    }

    init {
        if (!name.matches(NAME_REGEX)) throw RuntimeException("Invalid Name")
        if (name.length > MAX_NAME_LENGTH) throw RuntimeException("Name Too Long")
        if (name.length < MIN_NAME_LENGTH) throw RuntimeException("Name Too Short")
    }

    private val cards = HashMap<CardPrototype, Int>()

    var size = 0
        private set

    fun addCard(card: CardPrototype): Boolean {
        if (size >= MAX_DECK_LENGTH) return false

        val key = card.clone()
        this.cards[key]?.let {
            this.cards[card] = this.cards[card]!!.plus(1)
        } ?: run {
            this.cards[card] = 1
        }
        size++
        return true
    }

    fun removeCard(card: CardPrototype): Boolean = cards[card]?.let {
        when (cards[card]) {
            1 -> cards.remove(card)
            else -> cards[card]!!.minus(1)
        }
        size--
        return true
    } ?: run { false }

    fun removeCard(id: Int): Boolean = cards.keys.find { it.id == id }?.let {
        removeCard(it)
    } ?: run { false }

    fun cardsCopy(): HashMap<CardPrototype, Int> {
        val map = HashMap<CardPrototype, Int>()
        for (entry in cards.entries) {
            val key = entry.key
            if (key is MonsterPrototype) map[key.copy()] = entry.value
        }
        return map
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.appendln(name)
        for (entry in cards.entries) {
            sb.appendln("${entry.key.name}: x${entry.value}")
        }
        return sb.dropLast(1).toString()
    }
}