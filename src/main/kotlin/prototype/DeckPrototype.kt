package prototype

class DeckPrototype {

    companion object {
        private const val MIN_NAME_LENGTH = 1
        private const val MAX_NAME_LENGTH = 20
        private val NAME_REGEX = Regex("")
    }

    val name: String
    private val cards = HashMap<CardPrototype, Int>()
    var size = 0
        private set

    constructor(name: String) {
        this.name = name
    }

    constructor(name: String, jsonDeck: JsonDeck) {
        this.name = name
    }


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

    fun cardList(): Collection<CardPrototype> {
        return arrayListOf()
    }
}