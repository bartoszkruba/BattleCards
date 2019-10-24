package inAndOutputAdapter

import Card
import Game
import Monster
import SpellController
import factory.DeckFactory
import models.Deck
import models.Hand
import prototype.CardLoader

class GameController {
    private var playerOneName: String? = null
    private var playerTwoName: String? = null
    private lateinit var game: Game
    private var whiteTurn: Boolean? = null
    private var blackTurn: Boolean? = null
    private lateinit var decksList: CardLoader

    fun mainScreen() {
        OutputAdapter.printWelcome()
        playerOneName = userNameInput(1)
        playerTwoName = userNameInput(2)
        val playerOneDeck = decksOptions(playerOneName!!)
        val playerTwoDeck = decksOptions(playerTwoName!!)
        gameBoard(playerOneDeck!!, playerTwoDeck!!)
        OutputAdapter.printSpellDescriptions()
        startTheGame()
        OutputAdapter.printGameOver(game.getWinner()!!)
    }

    private fun startTheGame() {
        while (!game.checkGameOver()) {
            whiteTurn = game.currentPlayer() == game.whitePlayer
            blackTurn = game.currentPlayer() == game.blackPlayer
            while (whiteTurn as Boolean) {
                OutputAdapter.printBoard(game)
                val chosenOption = gameOptions()
                doTheChoice(chosenOption!!)
                whiteTurn = game.currentPlayer() == game.whitePlayer
            }
            while (blackTurn as Boolean) {
                OutputAdapter.printBoard(game)
                val chosenOption = gameOptions()
                doTheChoice(chosenOption!!)
                blackTurn = game.currentPlayer() == game.blackPlayer
            }
        }
    }

    private fun decksOptions(name: String): String? {
        var counter = 0
        decksList = CardLoader()
        val decksOption = decksList.listAvailableDecks()
        val availableDeckList = mutableMapOf<Int, String>()
        decksOption.forEach { deck ->
            counter++
            availableDeckList[counter] = deck
        }
        OutputAdapter.printAvailableDecks(availableDeckList)
        OutputAdapter.printChooseDeck(name)
        var chosenDeck = readLine()
        var validDeck = Input.readListAvailableDecks(chosenDeck!!, availableDeckList)
        while(validDeck == null){
            OutputAdapter.illegalInputInfo()
            chosenDeck = readLine()
            validDeck = Input.readListAvailableDecks(chosenDeck!!, availableDeckList)
            if(validDeck != null) return validDeck
        }
        return validDeck
    }

    private fun gameBoard(playerOneDeck: String, playerTwoDeck: String){
        val playerOneDeckPrototype = decksList.loadDeck(playerOneDeck)
        val playerTwoDeckPrototype = decksList.loadDeck(playerTwoDeck)

        val playerOneDeck = DeckFactory.createDeck(playerOneDeckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(playerTwoDeckPrototype)

        this.game = Game(playerOneDeck, playerTwoDeck, this.playerOneName!!, this.playerTwoName!!)
        OutputAdapter.printDeckPrototype(playerOneDeckPrototype, playerOneName!!)
        OutputAdapter.printDeckPrototype(playerTwoDeckPrototype, playerTwoName!!)
    }

    private fun doTheChoice(option: String): String? {
        when(option){
            Settings.MENU_OPTION_DRAW_CARD -> {
                game.drawCardFromDeck()
                val drawCard: Card = if(whiteTurn!!)
                    game.whitePlayer.hand.cardsInList()[game.whitePlayer.hand.cardsInList().size - 1]
                else
                    game.blackPlayer.hand.cardsInList()[game.blackPlayer.hand.cardsInList().size - 1]
                OutputAdapter.printDrawCardFromDeck(drawCard)
                OutputAdapter.printBoard(this.game)
            }
            Settings.MENU_OPTION_PLACE_CARD -> {
                val card = chooseCardToPlaceOnField()
                if(card.type == CardType.SPEll){
                    SpellController().cast(game, game.currentPlayer().hand.cardsInList().indexOf(card) + 1)
                }else{
                    game.placeCardOnField(card)
                    OutputAdapter.printBoard(this.game)
                }
            }
            Settings.MENU_OPTION_ATTACK_MONSTER ->{
                val cardToAttackWith = cardToAttackWith()
                val targetCard = targetCard()
                if (cardToAttackWith is Monster && targetCard is Monster)
                game.attackMonster(cardToAttackWith, targetCard)
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
        return Input.readGameOptions(gameOptions)
    }
}