package inAndOutputAdapter

import models.Player

class Input() {
    companion object {
        var playerOne: Player = Player("rami")
        var playerTwo: Player = Player("aalkan")

        fun readName(name: String): String? {
            var validUserName = userNameValidation(name)
            if(validUserName) return name else return null
        }

        fun menu(choice: String): String? {
            return if (choice == "Main Menu" || choice == "1") {
                choice
            } else if (choice == "Sub Menu" || choice == "2") {
                choice
            } else {
                null
            }
        }

        fun userNameValidation(name: String): Boolean {
            val regex = Regex("^[a-zA-Z]{1,9}")
            return name.length in 1..9 && regex.matches(name)
        }
    }
}