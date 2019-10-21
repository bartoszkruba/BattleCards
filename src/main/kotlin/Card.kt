import java.util.*

abstract class Card(open val name: String,
                    val type: CardType,
                    open val cardId: UUID
) {
    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        other as Card

        if (this.name != other.name) return false
        if (this.type != other.type) return false
        if (this.cardId != other.cardId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + cardId.hashCode()
        return result
    }
}