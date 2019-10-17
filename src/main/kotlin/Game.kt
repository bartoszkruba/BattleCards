import models.Deck
import models.Player

class Game(
        val player1Deck: Deck,
        val player2Deck: Deck,
        val player1Name: String,
        val player2Name: String) {
    lateinit var whitePlayer: Player;
    lateinit var blackPlayer: Player;
    var status:String = ""
    var turn:Int = 1

    init {

    }

    fun currentPlayer(): Player {
        return Player("1")
    }

    fun nextTurn(){
        turn++
    }

    fun attackMonster(attacker: Monster, toBeAttacked: Monster) {
        toBeAttacked.takeDamge(attacker)
    }

    fun printCurrentGame(){
        println("""

                     ${whitePlayer.name}

${whitePlayer.field}

_____________________________________________________

${blackPlayer.field}

                     ${blackPlayer.name}

        """.trimIndent())
//    TODO: Add ${messageLogger.lastLog()} to print
    }

    fun checkGameOver(){

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

