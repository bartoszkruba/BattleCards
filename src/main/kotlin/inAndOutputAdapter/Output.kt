package inAndOutputAdapter

class Output {

    fun printMainScreen(){
        var userInput = Input()
        print("Add player name: ")
        var userName = readLine()
        userInput.userNameInput(userName)

        print("Choose deck(name): ")
        var deckName = readLine()
        userInput.deckNameInput(deckName)

    }
}
