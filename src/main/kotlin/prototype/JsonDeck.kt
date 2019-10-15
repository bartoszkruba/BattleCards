package prototype


class JsonDeck {

    companion object {
        const val MAX_NAME_LENGTH = Settings.MAX_DECK_NAME_LENGTH
        const val MIN_NAME_LENGTH = Settings.MIN_DECK_NAME_LENGTH
        const val MAX_DECK_SIZE = Settings.DECK_SIZE
        val NAME_REGEX = Regex("[a-zA-z ]*")
    }

    val name: String
    private val records: HashMap<Int, Int>
    var size = 0
        private set

    constructor(name: String) {
        this.name = checkName(name)
        this.records = HashMap()
    }

    constructor(deckPrototype: DeckPrototype) {
        this.name = checkName(deckPrototype.name)
        this.records = HashMap()
        generateRecords(deckPrototype)
    }

    private fun checkName(name: String): String {
        if (name.length < MIN_NAME_LENGTH) throw RuntimeException("Name Too Short")
        if (name.length > MAX_NAME_LENGTH) throw RuntimeException("Name Too Long")
        if (!name.matches(NAME_REGEX)) throw RuntimeException("Invalid Name")
        return name
    }

    private fun generateRecords(deck: DeckPrototype) {
        for (entry in deck.cardsCopy().entries) {
            records[entry.key.id] = entry.value
            size += entry.value
        }
    }

    fun records(): HashMap<Int, Int> {
        val records = HashMap<Int, Int>()
        for (entry in this.records.entries) records[entry.key] = entry.value
        return records
    }

    fun addCard(id: Int): Boolean {
        if (size >= MAX_DECK_SIZE) return false

        records[id]?.let {
            records[id] = records[id]!!.plus(1)
        } ?: run {
            records[id] = 1
        }

        size++
        return true
    }

    fun addCard(cardPrototype: CardPrototype): Boolean = addCard(cardPrototype.id)

    fun removeCard(id: Int): Boolean = records[id]?.let {
        if (records[id] == 1) records.remove(id) else records[id] = records[id]!!.minus(1)
        size--
        return true
    } ?: run {
        return false
    }

    fun removeCard(card: CardPrototype): Boolean = removeCard(card.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JsonDeck

        if (name != other.name) return false
        if (records != other.records) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + records.hashCode()
        result = 31 * result + size
        return result
    }


}