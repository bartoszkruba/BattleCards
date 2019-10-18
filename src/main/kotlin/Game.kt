import models.Deck
import models.Player

class Game(
    val player1Deck: Deck,
    val player2Deck: Deck,
    val player1Name: String,
    val player2Name: String
) {
    lateinit var whitePlayer: Player;
    lateinit var blackPlayer: Player;
    var status: String = ""
    var turn: Int = 0

    init {

    }

    fun nextTurn() {
        turn++
    }

    fun attackMonster(attacker: Monster, toBeAttacked: Monster): Boolean {
        toBeAttacked.takeDamge(attacker)
        return toBeAttacked.isDead()
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

    fun checkGameOver() {

    }

    fun placeCardOnField(card: Card): Boolean {
        return false
    }

    fun drawCardFromDeck(): Boolean {
        return false
    }

    fun validMoves(): Map<Int, String> {
        return mapOf()
    }
}

