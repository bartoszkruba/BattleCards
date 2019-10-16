package inAndOutputAdapter

class Output {
    var userInput = Input()
    fun printMainScreen(){
        /*userNameInput(1);
        printAllDecks()
        chooseDeck(1);

        userNameInput(2);
        printAllDecks()
        chooseDeck(2);*/

        printMenus()
    }

    private fun printAllDecks() {
        val decks = listOf("one", "two", "three", "four")
        for ((index, value) in decks.withIndex()){
            println("${index+1}: ${value}")
        }
    }

    private fun chooseDeck(playerNumber: Int) {
        print("Choose a deck(player $playerNumber): ")
        var deckInput = readLine()
        var validDeckName = userInput.deckChoice(deckInput!!, playerNumber)
        while(!validDeckName){
            print("The deck does not exist! \nTry again: ")
            deckInput = readLine()
            if(userInput.deckChoice(deckInput!!, playerNumber)){
                validDeckName = true
            }
        }
    }

    private fun userNameInput(playerNumber: Int) {
        print("Add player $playerNumber name: ")
        var userName = readLine()
        var validUserName = userInput.userNameInput(userName!!, playerNumber)
        while(!validUserName){
            print("Wrong input of name \nTry again: ")
            userName = readLine()
            if(userInput.userNameInput(userName!!, playerNumber)){
                validUserName = true
            }
        }
    }

    private fun printMenus(){
        println("**** The game has just started ****")
        println("\n1. Main Menu")
        println("2. Sub Menu")
        print("\nAction: ")

        var choice = readLine()
        var validChoice = userInput.menu(choice!!)
        while(validChoice == null){
            print("Wrong choice \nTry again: ")
            choice = readLine()
            if(userInput.menu(choice!!) != null){
                validChoice = choice
            }
        }
    }
}
