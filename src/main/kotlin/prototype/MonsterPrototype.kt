package prototype


import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.RuntimeException


data class MonsterPrototype @JsonCreator constructor(
    @JsonProperty("id") override val id: Int,
    @JsonProperty("name") override val name: String,
    @JsonProperty("baseHealth") val baseHealth: Int,
    @JsonProperty("baseAttack") val baseAttack: Int
) : CardPrototype(id, name) {

    companion object {
        const val MAX_HEALTH = Settings.MAX_HEALTH
        const val MIN_HEALTH = Settings.MIN_HEALTH
        const val MAX_ATTACK = Settings.MAX_DAMAGE
        const val MIN_ATTACK = Settings.MIN_DAMAGE
    }

    init {
        if (name.length < MIN_NAME_LENGTH || name.length > MAX_NAME_LENGTH || !name.matches(NAME_REGEX))
            throw RuntimeException("Invalid Name")
        else if (baseHealth < MIN_HEALTH || baseHealth > MAX_HEALTH) throw RuntimeException("Invalid Base Health")
        else if (baseAttack < MIN_ATTACK || baseAttack > MAX_ATTACK) throw RuntimeException("Invalid Base Attack")
    }

    override fun equals(other: Any?) = super.equals(other)
    override fun hashCode() = super.hashCode()

    override fun clone(): CardPrototype = MonsterPrototype(
        id = id,
        name = name,
        baseHealth = baseHealth,
        baseAttack = baseAttack
    )
}