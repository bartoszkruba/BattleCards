import models.Deck
import models.Field
import models.Player

class Game(
    player1Deck: Deck,
    player2Deck: Deck,
    player1Name: String,
    player2Name: String
) {

    companion object {
        const val DECK_SIZE = Settings.DECK_SIZE
    }

    val whitePlayer: Player = Player(player1Name, player1Deck)
    val blackPlayer: Player = Player(player2Name, player2Deck);

    init {
        if (whitePlayer.deck.size() != DECK_SIZE || blackPlayer.deck.size() != DECK_SIZE)
            throw RuntimeException("Invalid deck length")

        whitePlayer.deck.shuffleDeck()
        blackPlayer.deck.shuffleDeck()
    }

    var status: String = ""
        private set
    var turn: Int = 1
        private set

    fun currentPlayer() = if (turn % 2 != 0) whitePlayer else blackPlayer

    fun oppositePlayer() = if (turn % 2 != 0) blackPlayer else whitePlayer

    fun nextTurn() {
        turn++
        whitePlayer.mana = Settings.PLAYER_MANA
        blackPlayer.mana = Settings.PLAYER_MANA
        whitePlayer.field.wakeUpMonsters()
        blackPlayer.field.wakeUpMonsters()
        checkGameOver()
    }

    fun attackMonster(attacker: Monster, toBeAttacked: Monster) {
        toBeAttacked.takeDamge(attacker)
        if (toBeAttacked.isDead()) {
            val player = if (currentPlayer() == whitePlayer) blackPlayer else whitePlayer
            player.field.removeCard(toBeAttacked)
        }
        currentPlayer().mana--
        attacker.sleeping = true
    }

    fun printCurrentGame() {
        println(
            """

                     ${whitePlayer.name}

${whitePlayer.field}

_____________________________________________________

${blackPlayer.field}

                     ${blackPlayer.name}

        """.trimIndent()
        )
//    TODO: Add ${messageLogger.lastLog()} to print
    }

    fun checkGameOver(): Boolean {
        return (whitePlayer.deck.size() == 0 && whitePlayer.field.size() == 0 && whitePlayer.hand.size() == 0)
                || (blackPlayer.deck.size() == 0 && blackPlayer.field.size() == 0 && blackPlayer.hand.size() == 0)
    }

    fun getWinner(): Player? {
        return if (whitePlayer.deck.size() == 0 && whitePlayer.field.size() == 0 && whitePlayer.hand.size() == 0) {
            blackPlayer
        } else if (blackPlayer.deck.size() == 0 && blackPlayer.field.size() == 0 && blackPlayer.hand.size() == 0) {
            whitePlayer
        } else {
            null
        }
    }

    fun placeCardOnField(card: Card): Boolean {
        val player = currentPlayer()
        val cardToPlace: Card
        try {
            cardToPlace = player.hand.removeCard(card)
        } catch (err: RuntimeException) {
            return false
        }
        return if (player.field.addCard(cardToPlace)) {
            player.mana--
            true
        } else {
            player.hand.addCard(cardToPlace)
            false
        }
    }

    fun drawCardFromDeck(): Boolean {
        val player = currentPlayer()
        if (player.hand.cardsInList().size < Settings.HAND_SIZE) {
            val drawnCard = player.deck.drawCard()
            if (drawnCard is Card) {
                return if (player.hand.addCard(drawnCard)) {
                    player.mana--
                    true
                } else {
                    player.deck.addCard(drawnCard)
                    false
                }
            }
        }
        return false
    }

    fun validMoves(): Map<Int, String> {
        val moves: HashMap<Int, String> = hashMapOf()
        var index = 0
        val currentPlayer: Player
        val opponent: Player

        when (turn % 2 != 0) {
            true -> {
                currentPlayer = whitePlayer
                opponent = blackPlayer
            }
            false -> {
                currentPlayer = blackPlayer
                opponent = whitePlayer
            }
        }

        if (currentPlayer.mana == 0) {
            moves[++index] = "End Round"
            return moves
        }

        if (!currentPlayer.field.empty && !opponent.field.empty)
            moves[++index] = "Attack Monster"

        if (!currentPlayer.hand.empty && currentPlayer.field.size() != Settings.FIELD_SIZE)
            moves[++index] = "Place Card"

        if (!currentPlayer.deck.empty && currentPlayer.hand.size() != Settings.HAND_SIZE)
            moves[++index] = "Draw Card"

        moves[++index] = "End Round"

        return moves
    }

    fun castFireball(cardIndex: Int, targetIndex: Int) {
        val currentPlayer = currentPlayer()
        val opponent = oppositePlayer()

        val opponentFieldSize = opponent.field.size()
        val currentPlayerHandSize = currentPlayer.hand.size()

        if (cardIndex < 1 || cardIndex > currentPlayerHandSize) throw RuntimeException("Invalid Input")
        if (targetIndex < 1 || targetIndex > opponentFieldSize) throw RuntimeException("Invalid Input")

        val cardInHand = currentPlayer.hand.cardsInList()[cardIndex - 1]

        if (cardInHand.type !== CardType.SPEll || (cardInHand as Spell).name != "Fireball")
            throw RuntimeException("Invalid Spell")

        val monster = opponent.field.cardsInList()[targetIndex - 1] as Monster
        monster.takeDamage(Settings.FIREBALL_DAMAGE)

        if (monster.isDead()) opponent.field.removeCard(monster)

        currentPlayer.mana--
        currentPlayer.hand.removeCard(cardInHand)
    }

    fun castHeal(cardIndex: Int, targetIndex: Int) {
        val currentPlayer = currentPlayer()

        val handSize = currentPlayer.hand.size()
        val fieldSize = currentPlayer.field.size()

        if (cardIndex < 1 || cardIndex > handSize) throw RuntimeException("Invalid Input")
        if (targetIndex < 1 || targetIndex > fieldSize) throw RuntimeException("Invalid Input")

        val cardInHand = currentPlayer.hand.cardsInList()[cardIndex - 1]

        if (cardInHand.type !== CardType.SPEll || (cardInHand as Spell).name != "Heal")
            throw RuntimeException("Invalid Spell")

        val monster = currentPlayer.field.cardsInList()[targetIndex - 1] as Monster
        monster.health += Settings.HEAL_VALUE

        currentPlayer.mana--
        currentPlayer.hand.removeCard(cardInHand)
    }

}

