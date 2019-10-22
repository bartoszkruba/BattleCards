import inAndOutputAdapter.Input
import inAndOutputAdapter.OutputAdapter

class SpellController {

    fun cast(game: Game, cardIndex: Int) {
        val currentPlayer = when (game.turn % 2 != 0) {
            true -> game.whitePlayer
            false -> game.blackPlayer
        }

        val card = currentPlayer.hand.cardsInList()[cardIndex - 1]

        if (card.type !== CardType.SPEll) return OutputAdapter.illegalInputInfo()

        when (card.name) {
            "Fireball" -> castFireball(game, cardIndex)
            "Heal" -> castHeal(game, cardIndex)
        }
    }

    fun castFireball(game: Game, cardIndex: Int) {
        val opponent = when (game.turn % 2 != 0) {
            true -> game.blackPlayer
            false -> game.whitePlayer
        }

        if (opponent.field.size() == 0) return OutputAdapter.illegalInputInfo()

        OutputAdapter.printChooseEnemyTarget(game)
        val targetIndex = Input.readEnemyTarget(game)

        game.castFireball(cardIndex, targetIndex)
    }

    fun castHeal(game: Game, cardIndex: Int) {
        val currentPlayer = when (game.turn % 2 != 0) {
            true -> game.whitePlayer
            false -> game.blackPlayer
        }

        if (currentPlayer.field.size() == 0) return OutputAdapter.illegalInputInfo()

        OutputAdapter.printChooseFriendlyTarget(game)
        val targetIndex = Input.readFriendlyTarget(game)

        game.castHeal(cardIndex, targetIndex)
    }
}