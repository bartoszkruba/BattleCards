package prototype

import com.fasterxml.jackson.annotation.JsonProperty

class SpellPrototype(
    @JsonProperty("id") override val id: Int,
    @JsonProperty("name") override val name: String
) : CardPrototype(id, name, CardType.SPEll) {

    init {
        if (name.length < MIN_NAME_LENGTH || name.length > MAX_NAME_LENGTH || !name.matches(NAME_REGEX))
            throw RuntimeException("Invalid Name")
    }

    override fun clone() = SpellPrototype(id = id, name = name)
}