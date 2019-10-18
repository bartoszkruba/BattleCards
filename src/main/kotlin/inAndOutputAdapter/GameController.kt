package inAndOutputAdapter

import Card
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
        var chosenOption = gameOptions()
        doTheChoice(chosenOption, playerDecks)

    }

    private fun printGameBoard(): Pair<Deck, Deck> {
        val cardLoader = CardLoader()

        val deckPrototype = cardLoader.loadDeck("test")
        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)

        this.game = Game(playerOneDeck, playerTwoDeck, this.playerOneName!!, this.playerTwoName!!)
        OutputAdapter.printBoard(game)
        OutputAdapter.printDeckPrototype(deckPrototype)
        return Pair(playerOneDeck, playerTwoDeck)
    }





    private fun doTheChoice(option: String?, playersDecks: Pair<Deck, Deck>): String? {
        val drawCard: Card?
        game.nextTurn()
        val whiteTurn = game.currentPlayer() == game.whitePlayer

        drawCard = if (whiteTurn) playersDecks.first.drawCard() else playersDecks.second.drawCard()
        when(option){
            "1", "draw card" -> {
                OutputAdapter.printDrawCardFromDeck(drawCard!!)
                game.drawCardFromDeck()
                printGameBoard()
            }
            "2", "place card" -> {
                chooseCardToPlaceOnField()
                //OutputAdapter.printBoard(this.game)
            }
            "3", "attack monster" ->{}
            "4", "end round" -> {}
            else -> return null
        }
        return option
    }



    private fun chooseCardToPlaceOnField() {
        OutputAdapter.printChooseCardToPlay(game)
        val chosenCardToPlace = readLine()
        Input.readCardToPlaceOnField(chosenCardToPlace!!, game.blackPlayer.hand)
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
        val gameOptions= mapOf( 1 to "draw card", 2 to "place card",3 to "attack monster", 4 to "end round")
        OutputAdapter.printGameOptions(gameOptions)

        var chosenOption = readLine()
        var validChoice = Input.readGameOptions(chosenOption!!, gameOptions)
        while(validChoice == null){
            OutputAdapter.illegalInputInfo()
            chosenOption = readLine()
            if(Input.readGameOptions(chosenOption!!, gameOptions) != null) {
                return chosenOption
            }
        }
        return chosenOption
    }
}