package prototype

// todo add type
abstract class CardPrototype(id: Int, name: String) {

    companion object {
        // todo get information from settings
        val MAX_NAME_LENGTH = 9
        val MIN_NAME_LENGTH = 1
        val NAME_REGEX = Regex("[a-zA-z ]*")
    }

    init {
        if (name.length < MIN_NAME_LENGTH || name.length > MAX_NAME_LENGTH || !name.matches(NAME_REGEX))
            throw RuntimeException("Invalid Name")
    }

}