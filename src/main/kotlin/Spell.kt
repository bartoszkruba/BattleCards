import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Spell(@JsonProperty("name") override val name: String) : Card(name, CardType.SPEll, UUID.randomUUID()) {

    init {
        if (name.length < MIN_NAME_LENGTH || name.length > MAX_NAME_LENGTH || !name.matches(NAME_REGEX))
            throw RuntimeException("Invalid Name")
    }

    companion object {
        val NAME_REGEX = Regex("[a-zA-z ]*")
        const val MIN_NAME_LENGTH = Settings.MIN_CARD_NAME_LENGTH
        const val MAX_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
    }
}