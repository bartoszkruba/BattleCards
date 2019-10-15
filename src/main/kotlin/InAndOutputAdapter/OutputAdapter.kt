package InAndOutputAdapter

class OutputAdapter {

    fun printMainScreen(){
        var userInput = inputAdapter()
        print("Add player name: ")
        var userName = readLine()
        userInput.userNameInput(userName)

        print("Choose deck(name): ")
        var deckName = readLine()
        userInput.deckNameInput(deckName)

    }
}
