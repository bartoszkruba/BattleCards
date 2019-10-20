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
            val regex = Regex("^[a-zåäöA-ZÅÄÖ]{1,9}") //ignore ÄÖÅ öäå ?????
            return name.length in 1..9 && regex.matches(name)
        }

        fun readGameOptions(option: String, optionsList:  Map<Int, String>): String? {
            optionsList.forEach { (k, v) ->
                 if (k.toString() == option|| option.toLowerCase() == v.toLowerCase()) return v
            }
            return null
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