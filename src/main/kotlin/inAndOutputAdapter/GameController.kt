package inAndOutputAdapter

import Game
import factory.DeckFactory
import prototype.CardLoader

class GameController {
    private var playerOneName: String? = null
    private var playerTwoName: String? = null

    fun printMainScreen() {
        OutputAdapter.printWelcome()
        this.playerOneName = userNameInput(1)
        this.playerTwoName = userNameInput(2)
        gameOptions()
    }

    private fun userNameInput(playerNumber:Int): String? {
        OutputAdapter.printEnterName(playerNumber)

        var playerName = readLine()
        val validPlayerName = Input.readName(playerName!!)
        while(validPlayerName == null){
            OutputAdapter.illegalInputInfo()
            playerName = readLine()
            if(Input.readName(playerName!!) != null) return playerName
        }
        return playerName
    }

    private fun gameOptions(){
        val cardLoader = CardLoader()

        val deckPrototype = cardLoader.loadDeck("test")
        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)

        val game = Game(playerOneDeck, playerTwoDeck, this.playerOneName!!, this.playerTwoName!!)
        OutputAdapter.printBoard(game)

        val gameOptions = game.validMoves()
        OutputAdapter.printGameOptions(gameOptions)

        var chosenOption = readLine()
        var validChoice = Input.readGameOptions(chosenOption!!, gameOptions)
        while(validChoice == null){
            OutputAdapter.illegalInputInfo()
            chosenOption = readLine()
            if(Input.readName(chosenOption!!) != null) validChoice = chosenOption
        }
    }
}