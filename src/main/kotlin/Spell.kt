import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.math.floor

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

    override fun toString(): String {
        val sb = StringBuilder()
        repeat((4 - floor(name.length * 0.5)).toInt()) { sb.append(" ") }
        var cardName = "$sb${name}"
        sb.clear()
        repeat(11 - cardName.length) { sb.append(" ") }
        cardName += sb
        cardName = Settings.ANSI_GREEN + cardName + Settings.ANSI_RESET

        val cyan = Settings.ANSI_CYAN
        val reset = Settings.ANSI_RESET

        val lines: Array<String> = arrayOf(
            "$cyan   ___     ",
            "  |   |    ",
            "  |   |    ",
            "  |___|    ",
            cardName + reset
        )

        return lines.joinToString("\n")
    }


}
