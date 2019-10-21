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
    private var whiteTurn: Boolean? = null

    fun printMainScreen() {
        //OutputAdapter.printWelcome()
        this.playerOneName = userNameInput(1)
        this.playerTwoName = userNameInput(2)
        val playerDecks = printGameBoard()

        whiteTurn = game.currentPlayer() == game.whitePlayer
        while (whiteTurn as Boolean) {
            val chosenOption = gameOptions()
            doTheChoice(chosenOption, playerDecks)
        }
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
                chooseCardToPlaceOnField()
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
        OutputAdapter.printChooseTarget(game)
        var chosenTargetCard = readLine()

        var validChoice = Input.readTargetCard(chosenTargetCard, game.whitePlayer.field)
        while(validChoice == null){
            OutputAdapter.illegalInputInfo()
            chosenTargetCard = readLine()
            validChoice = Input.readTargetCard(chosenTargetCard!!, game.whitePlayer.field)
            if(validChoice != null)
               return validChoice
        }
        return validChoice
    }

    private fun cardToAttackWith(): Card{
        OutputAdapter.printChooseCardToAttackWith(game)
        var chosenCardToAttackWith = readLine()

        var validChoice = Input.readChosenCardToAttackWith(chosenCardToAttackWith, game.blackPlayer.field)
        while(validChoice == null){
            OutputAdapter.illegalInputInfo()
            chosenCardToAttackWith = readLine()
            validChoice = Input.readChosenCardToAttackWith(chosenCardToAttackWith!!, game.blackPlayer.field)
            if(validChoice != null)
                return validChoice
        }
        return validChoice
    }


    private fun chooseCardToPlaceOnField(): Card{
        OutputAdapter.printChooseCardToPlay(game)
        var chosenCardToPlace = readLine()
        var validChoice = Input.readCardToPlaceOnField(chosenCardToPlace!!, game.blackPlayer.hand)
        while(validChoice == null){
            OutputAdapter.illegalInputInfo()
            chosenCardToPlace = readLine()
            validChoice = Input.readCardToPlaceOnField(chosenCardToPlace!!, game.blackPlayer.hand)
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