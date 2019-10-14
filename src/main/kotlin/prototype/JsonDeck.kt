package prototype


class JsonDeck {
    val name: String
    private val records: HashMap<Int, Int>
    var size = 0
        private set

    constructor(name: String) {
        this.name = name
        this.records = HashMap()
    }

    constructor(deckPrototype: DeckPrototype) {
        this.name = ""
        this.records = HashMap()
    }

    fun records(): HashMap<Int, Int> {
        return HashMap()
    }

    fun addCard(id: Int): Boolean {
        return false
    }

    fun addCard(cardPrototype: CardPrototype): Boolean {
        return false
    }

    fun removeCard(id: Int): Boolean {
        return false
    }

    fun removeCard(card: CardPrototype): Boolean {
        return false
    }
}