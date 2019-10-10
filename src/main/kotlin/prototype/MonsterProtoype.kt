package prototype

data class MonsterPrototype(
    val id: Int,
    val name: String,
    val baseHealth: Int,
    val baseAttack: Int
) : CardPrototype(id, name) {


}