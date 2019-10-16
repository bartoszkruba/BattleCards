import models.Player

class Game {
    var status:String = ""
    var turn:Int = 0
    var whitePlayer:Player = Player("White")
    var blackPlayer:Player = Player("Black")
    //var messageLogger:MessageLogger = MessageLogger()
    //var guiAdapter:GuiAdapter = GuiAdapter()

    fun nextTurn(){
        turn++
        printCurrentGame()
    }

    fun attackMonster(attacker: Monster, toBeAttacked: Monster): Boolean {
        toBeAttacked.takeDamge(attacker)
        return toBeAttacked.isDead()
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

    fun newGame(whitePlayer:Player,blackPlayer: Player){
        status = ""
        turn = 0
        this.whitePlayer = whitePlayer
        this.blackPlayer = blackPlayer
    }

    fun gameOver(){

    }
}

