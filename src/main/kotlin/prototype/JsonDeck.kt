package prototype


class JsonDeck {
    sealed class JsonDeckRecord(val id: Int, val count: Int)

    private val name: String
    private val records: ArrayList<JsonDeckRecord>
    var size = 0
        private set

    constructor(name: String) {
        this.name = name
        this.records = ArrayList()
    }

    constructor(name: String, deckPrototype: DeckPrototype) {
        this.name = name
        this.records = ArrayList()
    }

    fun addCard(id: Int): Boolean {
        return false
    }

    fun addCard(cardPrototype: CardPrototype) {

    }

    override fun toString(): String {
        return ""
    }

    fun removeCard(id: Int): Boolean {
        return false
    }
}