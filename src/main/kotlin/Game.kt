import models.Deck
import models.Player
import java.lang.RuntimeException

class Game(
    player1Deck: Deck,
    player2Deck: Deck,
    player1Name: String,
    player2Name: String
) {
    lateinit var whitePlayer: Player;
    lateinit var blackPlayer: Player;
    var status: String = ""
    var turn: Int = 1

    init {
        whitePlayer = Player(player1Name, player1Deck)
        blackPlayer = Player(player2Name, player2Deck)
    }

    fun currentPlayer() = if (turn % 2 != 0) whitePlayer else blackPlayer

    fun nextTurn() {
        turn++
        whitePlayer.mana = Settings.PLAYER_MANA
        blackPlayer.mana = Settings.PLAYER_MANA
        checkGameOver()
    }

    fun attackMonster(attacker: Monster, toBeAttacked: Monster) {
        toBeAttacked.takeDamge(attacker)
        if (toBeAttacked.isDead()) {
            val player = if (currentPlayer() == whitePlayer) blackPlayer else whitePlayer
            player.field.removeCard(toBeAttacked)
        }
        currentPlayer().mana--
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
        return (whitePlayer.deck.cardsInList().size == 0 && whitePlayer.field.cardsInList().size == 0 && whitePlayer.hand.cardsInList().size == 0)
                || (blackPlayer.deck.cardsInList().size == 0 && blackPlayer.field.cardsInList().size == 0 && blackPlayer.hand.cardsInList().size == 0)
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
        if (player.hand.cardsInList().size < Settings.HAND_SIZE){
            val drawnCard = player.deck.drawCard()
            if(drawnCard is Card){
                return if(player.hand.addCard(drawnCard)){
                    player.mana--
                    true
                }else{
                    player.deck.addCard(drawnCard)
                    false
                }
            }
        }
        return false
    }

    fun validMoves(): Map<Int, String> {
        var moves: HashMap<Int, String> = hashMapOf()
        var index = 0
        if(!currentPlayer().field.empty) moves[++index] = "Attack Monster"
        if(!currentPlayer().hand.empty) moves[++index] = "Place Card"
        if(!currentPlayer().deck.empty) moves[++index] = "Draw Card"
        moves[++index] = "End Round"
        return moves
    }
}

