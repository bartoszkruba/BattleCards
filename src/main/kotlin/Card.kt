import java.util.*

abstract class Card(val name: String,
                    val type: CardType,
                    val cardId: UUID
) {
    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        other as Card

        if (!this.name.equals(other.name)) return false
        if (!this.type.equals(other.type)) return false
        if (!this.cardId.equals(other.cardId)) return false

        return true
    }
}