package inAndOutputAdapter

import models.Player

class Input() {
    private var playerOne:Player = Player("rami")
    private var playerTwo:Player = Player("aalkan")

    fun userNameInput(name: String, playerNumber: Int): Boolean {
        if (playerNumber in 1..2) {
        var validUserName = userNameValidation(name)
            if (validUserName) {
                if (playerNumber == 1) playerOne.name = name else playerTwo.name = name
            }
            return validUserName
        }
        return false
    }

    fun deckChoice(deckInput: String, playerNumber: Int): Boolean {
        if (playerNumber in 1..2) {
            var validDeckName = deckInputValidation(deckInput)
            if(validDeckName){
                //if (playerNumber == 1) playerOne.deck = Deck()!! else playerTwo.deck = Deck()!!
            }
            return validDeckName
        }
        return false
    }

    private fun userNameValidation(name: String):Boolean{
        val regex = Regex("^[a-zA-Z]{1,9}")
        return name.length in 1..9 && regex.matches(name)

    }

    private fun deckInputValidation(name: String):Boolean{
        val regex = Regex("^\\d")
        val decks = listOf("one", "two", "three", "four")
        if (regex.matches(name)) {
            for ((index) in decks.withIndex()) {
                if (index == name.toInt() - 1) return true
            }
        } else {
            for ((index, value) in decks.withIndex()) {
                if (value == name) return true
            }
        }
        return false
    }
}