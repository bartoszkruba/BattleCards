import java.util.*

abstract class Card(
    open val name: String,
    val type: CardType,
    open val cardId: UUID
)