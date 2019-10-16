package inAndOutputAdapter

class Output {
    var userInput = Input()
    fun printMainScreen(){
        userNameInput(1);
        printAllDecks()
        chooseDeck(1);

        userNameInput(2);
        printAllDecks()
        chooseDeck(2);
    }

    private fun printAllDecks() {
        val decks = listOf("one", "two", "three", "four")
        for ((index, value) in decks.withIndex()){
            println("${index+1}: ${value}")
        }
    }

    private fun chooseDeck(playerNumber: Int) {
        print("Choose a deck(player $playerNumber): ")
        var deckName = readLine()
        var validDeckName = userInput.deckNameInput(deckName, playerNumber)
        while(!validDeckName){
            print("The deck does not exist! \nTry again: ")
            deckName = readLine()
            if(userInput.deckNameInput(deckName, playerNumber)){
                validDeckName = true
            }
        }
    }

    private fun userNameInput(playerNumber: Int) {
        print("Add player $playerNumber name: ")
        var userName = readLine()
        var validUserName = userInput.userNameInput(userName, playerNumber)
        while(!validUserName){
            print("Wrong input of name \nTry again: ")
            userName = readLine()
            if(userInput.userNameInput(userName, playerNumber)){
                validUserName = true
            }
        }
    }
}
