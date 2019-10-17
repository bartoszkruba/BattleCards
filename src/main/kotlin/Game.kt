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

    fun currentPlayer() = if(turn % 2 != 0) whitePlayer else blackPlayer

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

    fun checkGameOver():Boolean{
       return (whitePlayer.deck.cardsInList().size == 0 && whitePlayer.field.cardsInList().size == 0 && whitePlayer.hand.cardsInList().size == 0)
           || (blackPlayer.deck.cardsInList().size == 0 && blackPlayer.field.cardsInList().size == 0 && blackPlayer.hand.cardsInList().size == 0)
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

