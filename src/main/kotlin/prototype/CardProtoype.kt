package prototype

abstract class CardPrototype(open val id: Int, open val name: String) : Clonable {

    companion object {
        // todo get information from settings
        val MAX_NAME_LENGTH = 9
        val MIN_NAME_LENGTH = 1
        val NAME_REGEX = Regex("[a-zA-z ]*")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardPrototype

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}