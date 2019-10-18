package inAndOutputAdapter

import Game
import factory.DeckFactory
import models.Deck
import prototype.CardLoader

class GameController {
    private var playerOneName: String? = null
    private var playerTwoName: String? = null
    lateinit var game:Game

    fun printMainScreen() {
        //OutputAdapter.printWelcome()
        this.playerOneName = userNameInput(1)
        this.playerTwoName = userNameInput(2)
        var playerDecks = printGameBoard()
        val chosenOption = gameOptions()
        doTheChoice(chosenOption, playerDecks)

    }

    private fun printGameBoard(): Pair<Deck, Deck> {
        val cardLoader = CardLoader()

        val deckPrototype = cardLoader.loadDeck("test")
        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)

        this.game = Game(playerOneDeck, playerTwoDeck, this.playerOneName!!, this.playerTwoName!!)
        OutputAdapter.printBoard(game)
        return Pair(playerOneDeck, playerTwoDeck)
    }

    private fun doTheChoice(option: String?, playersDecks: Pair<Deck, Deck>) {
        val whiteTurn = this.game.turn % 2 != 0
        when(option){
            "1", "draw" -> OutputAdapter.printDrawCardFromDeck(playersDecks.first.drawCard()!!)
            //"2", "put" -> if(whiteTurn) OutputAdapter.
        }
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

    private fun gameOptions(): String? {
        //val gameOptions = game.validMoves()
        val gameOptions= mapOf( 1 to "Draw", 2 to "PUT",3 to "Attack", 4 to "Pass")

        OutputAdapter.printGameOptions(gameOptions)

        var chosenOption = readLine()
        val validChoice = Input.readGameOptions(chosenOption!!, gameOptions)
        while(validChoice == null){
            OutputAdapter.illegalInputInfo()
            chosenOption = readLine()
            if(Input.readName(chosenOption!!) != null) {
                return chosenOption
            }
        }
        return chosenOption
    }
}