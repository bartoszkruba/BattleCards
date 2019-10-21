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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as SpellPrototype

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id
        return result
    }
}