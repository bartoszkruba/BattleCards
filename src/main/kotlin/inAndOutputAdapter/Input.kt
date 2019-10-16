package inAndOutputAdapter

import models.Player

class Input() {
    private var playerOne:Player = Player("rami")
    private var playerTwo:Player = Player("aalkan")

    fun userNameInput(name: String?, playerNumber: Int): Boolean {
        return userNameValidation(name)
    }

    fun deckNameInput(deckName: String?, playerNumber: Int): Boolean {
        return checkDeckName(deckName)
    }

    private fun userNameValidation(name: String?):Boolean{
        val regex = Regex("^[a-zA-Z]{1,9}")
        return if (name != null) {
            name.length in 1..9 && regex.matches(name)
        }else{
            false
        }
    }

    private fun checkDeckName(name: String?):Boolean{
        val regex = Regex("^\\d")
        val decks = listOf("one", "two", "three", "four")
        if(name != null) {
            if (regex.matches(name!!)) {
                for ((index, value) in decks.withIndex()) {
                    if (index == name!!.toInt() - 1) {
                        return true
                    }
                }
            } else {
                for ((index, value) in decks.withIndex()) {
                    if (value == name) {
                        return true
                    }
                }
            }
        }
        return false
    }
}