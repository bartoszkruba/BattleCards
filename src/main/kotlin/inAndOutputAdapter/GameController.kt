package inAndOutputAdapter

import Game
import factory.DeckFactory
import prototype.CardLoader

class GameController {

    fun printMainScreen() {
        OutputAdapter.printWelcome()
        OutputAdapter.printEnterName(1)
        var playerOneName = readLine()
        var validPlayerOneName = Input.readName(playerOneName!!)
        while(validPlayerOneName == null){
            OutputAdapter.illegalInputInfo()
            playerOneName = readLine()
            if(Input.readName(playerOneName!!) != null){
                validPlayerOneName = playerOneName
            }
        }

        OutputAdapter.printEnterName(2)
        var playerTwoName = readLine()
        var validPlayerTwoName = Input.readName(playerTwoName!!)
        while(validPlayerTwoName == null){
            OutputAdapter.illegalInputInfo()
            playerTwoName = readLine()
            if(Input.readName(playerTwoName!!) != null){
                validPlayerTwoName = playerTwoName
            }
        }

        val cardLoader = CardLoader()

        val deckPrototype = cardLoader.loadDeck("test")
        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)

        var game = Game(playerOneDeck, playerTwoDeck, playerOneName!!, playerTwoName!!)
        OutputAdapter.printBoard(game)

        val gameOptions = game.validMoves()
        OutputAdapter.printGameOptions(gameOptions)
        var chosenOption = readLine()

        var validChoice = Input.readGameOptions(chosenOption!!, gameOptions)
        while(validChoice == null){
            OutputAdapter.illegalInputInfo()
            chosenOption = readLine()
            if(Input.readName(chosenOption!!) != null){
                validChoice = chosenOption
            }
        }
    }
}