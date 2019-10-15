package inAndOutputAdapter

class Output {

    fun printMainScreen(){
        var userInput = Input()
        print("Add player name: ")
        var userName = readLine()
        var validUserName = userInput.userNameInput(userName)
        while(!validUserName){
            print("Wrong input of name \nTry again: ")
            userName = readLine()
            if(userInput.userNameInput(userName)){
                validUserName = true
            }
        }

        val decks = listOf("one", "two", "three", "four")
        for ((index, value) in decks.withIndex()){
           println("${index+1}: ${value}")
        }

        print("Choose a deck: ")
        var deckName = readLine()
        var validDeckName = userInput.deckNameInput(deckName)
        while(!validDeckName){
            print("The deck does not exist! \nTry again: ")
            deckName = readLine()
            if(userInput.checkDeckName(deckName)){
                validDeckName = true
            }
        }

    }
}
