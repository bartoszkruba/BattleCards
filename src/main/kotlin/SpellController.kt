import inAndOutputAdapter.Input
import inAndOutputAdapter.OutputAdapter

class SpellController {

    fun cast(game: Game, card: Card) {
        when (card.name) {
            "Fireball" -> castFireball(game, card)
            "Heal" -> castHeal(game, card)
        }
    }

    fun castFireball(game: Game, card: Card) {

    }

    fun castHeal(game: Game, card: Card) {

    }
}