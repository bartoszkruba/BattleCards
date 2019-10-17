import models.Deck
import models.Player

class Game(
        player1Deck: Deck,
        player2Deck: Deck,
        player1Name: String,
        player2Name: String) {
    lateinit var whitePlayer: Player;
    lateinit var blackPlayer: Player;
    var status:String = ""
    var turn:Int = 1

    init {
        whitePlayer = Player(player1Name,player1Deck)
        blackPlayer = Player(player2Name,player2Deck)
    }

    fun currentPlayer(): Player {
        return Player("1")
    }

    fun nextTurn(){
        turn++
        whitePlayer.mana = Settings.PLAYER_MANA
        blackPlayer.mana = Settings.PLAYER_MANA
        checkGameOver()
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

