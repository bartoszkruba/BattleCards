package inAndOutputAdapter

import Card
import models.Field
import models.Hand
import Monster
import Game

class Input() {
    companion object {

        fun readName(name: String): String? {
            var validUserName = userNameValidation(name)
            if(validUserName) return name else return null
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
            //var cardsInHand= hand.cardsInList()
            return null
        }

        fun readChosenCardToAttackWith(game: Game): Card {
            var invalidInput = true
            var choosenCard:Card = Monster("Ugly Placeholder",0,0)
            OutputAdapter.printChooseCardToAttackWith(game)
            do {
                val input = readLine()
                val choosenCardIndex:Int
                if (input is String){
                    try {
                        choosenCardIndex = input.toInt()
                    }catch (err:Exception){
                        OutputAdapter.illegalInputInfo()
                        continue
                    }
                    if (choosenCardIndex < game.currentPlayer().field.size()){
                        choosenCard = game.currentPlayer().field.cardsInList()[choosenCardIndex]
                        if(choosenCard is Monster){
                            if (choosenCard.sleeping){
                                OutputAdapter.illegalInputInfo()
                                continue
                            }
                        }
                        return choosenCard
                    }else{
                        OutputAdapter.illegalInputInfo()
                        continue
                    }
                }else{
                    OutputAdapter.illegalInputInfo()
                    continue
                }
            }while (invalidInput)
            return choosenCard
        }

        fun readTargetCard(game: Game): Card{
            var invalidInput = true
            var choosenCard:Card = Monster("Ugly Placeholder",0,0)
            OutputAdapter.printChooseTarget(game)
            do {
                val input = readLine()
                val choosenCardIndex:Int
                if (input is String){
                    try {
                        choosenCardIndex = input.toInt()
                    }catch (err:Exception){
                        OutputAdapter.illegalInputInfo()
                        continue
                    }
                    if (choosenCardIndex < game.currentPlayer().field.size()){
                        choosenCard = game.oppositePlayer().field.cardsInList()[choosenCardIndex]
                        return choosenCard
                    }else{
                        OutputAdapter.illegalInputInfo()
                        continue
                    }
                }else{
                    OutputAdapter.illegalInputInfo()
                    continue
                }
            }while (invalidInput)
            return choosenCard
        }
    }
}