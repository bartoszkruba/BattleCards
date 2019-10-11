package prototype


import kotlin.RuntimeException

data class MonsterPrototype(
    val id: Int,
    val name: String,
    val baseHealth: Int,
    val baseAttack: Int
) : CardPrototype(id, name) {

    companion object {

        // todo load from settings
        val MAX_HEALTH = 10
        val MIN_HEALTH = 1
        val MAX_ATTACK = 10
        val MIN_ATTACK = 1
    }

    init {
        if (baseHealth < MIN_HEALTH || baseHealth > MAX_HEALTH) throw RuntimeException("Invalid Base Health")
        else if (baseAttack < MIN_ATTACK || baseAttack > MAX_ATTACK) throw RuntimeException("Invalid Base Attack")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MonsterPrototype

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}