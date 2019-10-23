package inAndOutputAdapter

import Card
import models.Hand
import Monster
import Game

class Input() {
    companion object {

        fun readName(name: String): String? {
            val validUserName = userNameValidation(name)
            return if(validUserName) name else null
        }

        private fun userNameValidation(name: String): Boolean {
            val regex = Regex("^[a-zåäöA-ZÅÄÖ]{1,9}")
            return name.length in 1..9 && regex.matches(name)
        }

        fun readGameOptions(optionsList:  Map<Int, String>): String? {
            while (true) {
                OutputAdapter.printGameOptions(optionsList)
                try {
                    val choice = readLine()

                    for (value in optionsList.values) {
                        if (choice == value) return choice
                    }
                    if (optionsList[choice!!.toInt()] == null) {
                        OutputAdapter.illegalInputInfo()
                    } else {
                        return optionsList[choice.toInt()]!!
                    }
                } catch (e: Exception) {
                    OutputAdapter.illegalInputInfo()
                }
            }
        }

        fun readCardToPlaceOnField(chosenCardToPlace: String, hand: Hand): Card? {
            try {
                if(chosenCardToPlace.toInt() in 1..hand.size()){
                    return hand.cardsInList()[chosenCardToPlace.toInt()-1]
                }
            }catch (e: Exception) {
                return null
            }
            return null
        }

        fun printErrorField(game: Game, current: Boolean) {
            OutputAdapter.printBoard(game)
            OutputAdapter.illegalInputInfo()
            if(current) OutputAdapter.printChooseCardToAttackWith(game)
            else        OutputAdapter.printChooseTarget(game)
        }

        fun readChosenCardToAttackWith(game: Game): Card {
            var invalidInput = true
            var choosenCard:Card = Monster("Ugly",1,1)
            OutputAdapter.printBoard(game)
            OutputAdapter.printChooseCardToAttackWith(game)
            do {
                val input = readLine()
                val choosenCardIndex:Int
                if (input is String){
                    try {
                        choosenCardIndex = input.toInt() -1
                    }catch (err:Exception){
                        printErrorField(game, true)
                        continue
                    }
                    if (choosenCardIndex < game.currentPlayer().field.size()){
                        choosenCard = game.currentPlayer().field.cardsInList()[choosenCardIndex]
                        if(choosenCard is Monster){
                            if (choosenCard.sleeping){
                                printErrorField(game, true)
                                continue
                            }
                        }
                        return choosenCard
                    }else{
                        printErrorField(game, true)
                        continue
                    }
                }else{
                    printErrorField(game, true)
                    continue
                }
            }while (invalidInput)
            return choosenCard
        }

        fun readTargetCard(game: Game): Card{
            var invalidInput = true
            var choosenCard:Card = Monster("Ugly",1,1)
            OutputAdapter.printBoard(game)
            OutputAdapter.printChooseTarget(game)
            do {
                val input = readLine()
                val choosenCardIndex:Int
                if (input is String){
                    try {
                        choosenCardIndex = input.toInt() -1
                    }catch (err:Exception){
                        printErrorField(game, false)
                        continue
                    }
                    if (choosenCardIndex < game.oppositePlayer().field.size()){
                        choosenCard = game.oppositePlayer().field.cardsInList()[choosenCardIndex]
                        return choosenCard
                    }else{
                        printErrorField(game, false)
                        continue
                    }
                }else{
                    printErrorField(game, false)
                    continue
                }
            }while (invalidInput)
            return choosenCard
        }

        fun readListAvailableDecks(choice: String, decksList: MutableMap<Int, String>): String? {
            try {
                for (value in decksList.values) {
                    if (choice == value) return choice
                }
                return if (decksList[choice.toInt()] == null) {
                    null
                } else {
                    decksList[choice.toInt()]!!
                }
            }catch (e: Exception) {
                return null
            }
        }
    }
}