package inAndOutputAdapter

import Card
import models.Field
import models.Hand

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

        fun readChosenCardToAttackWith(chosenCardToAttackWith: String?, field: Field): Card? {
            return null

        }

        fun readTargetCard(chosenTargetCard: String?, field: Field): Card? {
            return null
        }
    }
}