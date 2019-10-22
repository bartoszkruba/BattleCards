package inAndOutputAdapter

import Card
import Game
import Monster
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

    fun printMainScreen() {
        //OutputAdapter.printWelcome()
        playerOneName = userNameInput(1)
        playerTwoName = userNameInput(2)
        val playerOneDeck = decksOptions(playerOneName!!)
        val playerTwoDeck = decksOptions(playerTwoName!!)
        val playersDecks = gameBoard(playerOneDeck!!, playerTwoDeck!!)
        startTheGame(playersDecks)
        OutputAdapter.printGameOver(game.getWinner()!!)
    }

    private fun startTheGame(playersDecks: Pair<Deck, Deck>) {
        while (!game.checkGameOver()) {
            whiteTurn = game.currentPlayer() == game.whitePlayer
            blackTurn = game.currentPlayer() == game.blackPlayer
            while (whiteTurn as Boolean) {
                OutputAdapter.printBoard(game)
                val chosenOption = gameOptions()
                doTheChoice(chosenOption, playersDecks)
                whiteTurn = game.currentPlayer() == game.whitePlayer
            }
            while (blackTurn as Boolean) {
                OutputAdapter.printBoard(game)
                val chosenOption = gameOptions()
                doTheChoice(chosenOption, playersDecks)
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
        var validDeck = Input.readlistAvailableDecks(chosenDeck!!, availableDeckList)
        while(validDeck == null){
            OutputAdapter.illegalInputInfo()
            chosenDeck = readLine()
            validDeck = Input.readlistAvailableDecks(chosenDeck!!, availableDeckList)
            if(validDeck != null) return validDeck
        }
        return validDeck
    }

    private fun gameBoard(playerOneDeck: String, playerTwoDeck: String): Pair<Deck, Deck> {
        val playerOneDeckPrototype = decksList.loadDeck(playerOneDeck)
        val playerTwoDeckPrototype = decksList.loadDeck(playerTwoDeck)

        val playerOneDeck = DeckFactory.createDeck(playerOneDeckPrototype)
        val playerTwoDeck = DeckFactory.createDeck(playerTwoDeckPrototype)

        this.game = Game(playerOneDeck, playerTwoDeck, this.playerOneName!!, this.playerTwoName!!)
        OutputAdapter.printDeckPrototype(playerOneDeckPrototype, playerOneName!!)
        OutputAdapter.printDeckPrototype(playerTwoDeckPrototype, playerTwoName!!)
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