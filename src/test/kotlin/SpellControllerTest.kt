import factory.CardFactory
import inAndOutputAdapter.Input
import inAndOutputAdapter.OutputAdapter
import models.Deck
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import prototype.MonsterPrototype

internal class SpellControllerTest {

    companion object {
        const val MAX_DECK_NAME_LENGTH = Settings.MAX_DECK_NAME_LENGTH
        const val MAX_CARD_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        const val DECK_SIZE = Settings.DECK_SIZE
        const val HAND_SIZE = Settings.HAND_SIZE
        const val FIELD_SIZE = Settings.FIELD_SIZE
        const val MAX_HEALTH = Settings.MAX_HEALTH
        const val MAX_ATTACK = Settings.MAX_DAMAGE

        const val ID_ONE = 1
        const val ID_TWO = 2
        const val ID_THREE = 3
        const val ID_FOUR = 4

        const val PLAYER_NAME_ONE = "aaaa"
        const val PLAYER_NAME_TWO = "bbb"
    }

    val MONSTER_NAME_ONE: String
    val MONSTER_NAME_TWO: String
    val MONSTER_NAME_THREE: String

    val DECK_NAME_ONE: String


    init {
        val sbA = StringBuilder()
        val sbB = StringBuilder()
        val sbC = StringBuilder()
        val sbD = StringBuilder()

        repeat(MAX_CARD_NAME_LENGTH) {
            sbA.append("a")
            sbB.append("b")
            sbC.append("c")
        }

        repeat(MAX_DECK_NAME_LENGTH) {
            sbD.append("d")
        }

        MONSTER_NAME_ONE = sbA.toString()
        MONSTER_NAME_TWO = sbB.toString()
        MONSTER_NAME_THREE = sbC.toString()

        DECK_NAME_ONE = sbD.toString()
    }

    lateinit var outputAdapter: OutputAdapter
    lateinit var input: Input
    lateinit var spellController: SpellController

    @BeforeEach
    internal fun setUp() {
        outputAdapter = Mockito.mock(OutputAdapter::class.java)
        input = Mockito.mock(Input::class.java)
        spellController = SpellController(input, outputAdapter)
    }


    fun prepareGame(): Game {
        val prototypeOne = MonsterPrototype(ID_ONE, MONSTER_NAME_ONE, MAX_HEALTH, MAX_ATTACK)

        val deckOne = Deck()
        val deckTwo = Deck()

        repeat(DECK_SIZE) {
            deckOne.addCard(CardFactory.createCard(prototypeOne))
            deckTwo.addCard(CardFactory.createCard(prototypeOne))
        }

        val game = Game(deckOne, deckTwo, PLAYER_NAME_ONE, PLAYER_NAME_TWO)

        val bPlayer = game.blackPlayer

        val wPlayer = game.whitePlayer

        repeat(HAND_SIZE) {
            wPlayer.hand.addCard(wPlayer.deck.drawCard()!!)
            bPlayer.hand.addCard(bPlayer.deck.drawCard()!!)
        }

        repeat(FIELD_SIZE) {
            wPlayer.field.addCard(wPlayer.deck.drawCard()!!)
            bPlayer.field.addCard(bPlayer.deck.drawCard()!!)
        }

        return game
    }

    @Test
    internal fun `Casting Fireball goes right, white turn`() {

    }

    @Test
    internal fun `Casting Fireball goes right, black turn`() {

    }

    @Test
    internal fun `Casting Heal goes right, white turn`() {

    }

    @Test
    internal fun `Casting Heal goes right, black turn`() {

    }

    @Test
    internal fun `Casting Hibernate goes right, white turn`() {

    }

    @Test
    internal fun `Casting Hibernate goes right, black turn`() {

    }

    @Test
    internal fun `Casting Fireball, no target to choose, white turn`() {

    }

    @Test
    internal fun `Casting Fireball, no target to choose, black turn`() {

    }

    @Test
    internal fun `Casting Heal, no target to choose, white turn`() {

    }

    @Test
    internal fun `Casting Heal, no target to choose, black turn`() {

    }

    @Test
    internal fun `Casting Hibernate, no target to choose, white turn`() {

    }

    @Test
    internal fun `Casting Hibernate, no target to choose black turn`() {

    }
}