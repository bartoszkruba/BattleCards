package inAndOutputAdapter

import Card
import Game
import factory.DeckFactory
import models.Deck
import models.Field
import models.Hand
import prototype.CardLoader

class GameController {
    private var playerOneName: String? = null
    private var playerTwoName: String? = null
    lateinit var game:Game
    private var whiteTurn: Boolean? = null
    private var blackTurn: Boolean? = null

    fun printMainScreen() {
        //OutputAdapter.printWelcome()
        this.playerOneName = userNameInput(1)
        this.playerTwoName = userNameInput(2)
        val playerDecks = printGameBoard()
        while (!game.checkGameOver()) {
            whiteTurn = game.currentPlayer() == game.whitePlayer
            blackTurn = game.currentPlayer() == game.blackPlayer
            while (whiteTurn as Boolean) {
                OutputAdapter.printBoard(game)
                val chosenOption = gameOptions()
                doTheChoice(chosenOption, playerDecks)
                whiteTurn = game.currentPlayer() == game.whitePlayer
            }
            while (blackTurn as Boolean) {
                OutputAdapter.printBoard(game)
                val chosenOption = gameOptions()
                doTheChoice(chosenOption, playerDecks)
                blackTurn = game.currentPlayer() == game.blackPlayer
            }
        }
    }

    private fun printGameBoard(): Pair<Deck, Deck> {
        val cardLoader = CardLoader()

        val deckPrototype = cardLoader.loadDeck("Standard")
        val playerOneDeck = DeckFactory.createDeck(deckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(deckPrototype)

        this.game = Game(playerOneDeck, playerTwoDeck, this.playerOneName!!, this.playerTwoName!!)
        OutputAdapter.printBoard(game)
        OutputAdapter.printDeckPrototype(deckPrototype)
        return Pair(playerOneDeck, playerTwoDeck)
    }

    private fun doTheChoice(option: String?, playersDecks: Pair<Deck, Deck>): String? {
        when(option!!){
            Settings.MENU_OPTION_DRAW_CARD -> {
                game.drawCardFromDeck()
                val drawCard: Card = if(whiteTurn!!)
                    game.whitePlayer.hand.cardsInList().get(game.whitePlayer.hand.cardsInList().size - 1)
                else
                    game.blackPlayer.hand.cardsInList().get(game.blackPlayer.hand.cardsInList().size - 1)
                OutputAdapter.printDrawCardFromDeck(drawCard)
                OutputAdapter.printBoard(this.game)
            }
            Settings.MENU_OPTION_PLACE_CARD -> {
                val card = chooseCardToPlaceOnField()
                game.placeCardOnField(card)
                OutputAdapter.printBoard(this.game)
            }
            Settings.MENU_OPTION_ATTACK_MONSTER ->{
                cardToAttackWith()
                targetCard()
            }
            Settings.MENU_OPTION_END_ROUND -> {
                game.nextTurn()
            }
            else -> return null
        }
        return option
    }

    private fun targetCard(): Card {
        return Input.readTargetCard(game)
    }

    private fun cardToAttackWith(): Card{
        return Input.readChosenCardToAttackWith(game)
    }


    private fun chooseCardToPlaceOnField(): Card{
        val playerHand: Hand = if(whiteTurn!!) game.whitePlayer.hand else game.blackPlayer.hand
        OutputAdapter.printChooseCardToPlay(game)
        var chosenCardToPlace = readLine()
        var validChoice = Input.readCardToPlaceOnField(chosenCardToPlace!!, playerHand)
        while(validChoice == null){
            OutputAdapter.illegalInputInfo()
            chosenCardToPlace = readLine()
            validChoice = Input.readCardToPlaceOnField(chosenCardToPlace!!, playerHand)
            if(validChoice != null) return validChoice
        }
        return validChoice
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
        val gameOptions = game.validMoves()
        //val gameOptions= mapOf( 1 to "draw card", 2 to "place card",3 to "attack monster", 4 to "end round")
        return Input.readGameOptions(gameOptions)
    }
}