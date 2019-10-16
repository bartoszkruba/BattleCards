import models.Player

class Game {
    var status:String = ""
    var turn:Int = 0
    var whitePlayer:Player = Player("WhitePlayer")
    var blackPlayer:Player = Player("BlackPlayer")
    //var messageLogger:MessageLogger = MessageLogger()
    //var guiAdapter:GuiAdapter = GuiAdapter()

    fun nextTurn(){

    }

    fun attackMonster(attacker:Card,toBeAttacked:Card){

    }

    fun printCurrentGame(){

    }

    fun newGame(whitePlayer:Player,blackPlayer: Player){

    }

    fun gameOver(){

    }
}

