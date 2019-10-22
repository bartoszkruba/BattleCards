import inAndOutputAdapter.Input
import inAndOutputAdapter.OutputAdapter

// This dependency injection is only for testing purposes
class SpellController(input: Input = Input(), outputAdapter: OutputAdapter = OutputAdapter()) {

    fun cast(game: Game, card: Card) {

    }

    fun castFireball(game: Game, card: Card) {

    }

    fun castHibernate(game: Game, card: Card) {

    }

    fun castHeal(game: Game, card: Card) {

    }
}