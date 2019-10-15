package inAndOutputAdapter

class Output {

    fun printMainScreen(){
        var userInput = Input()
        print("Add player name: ")
        var userName = readLine()
        var validUserName = userInput.userNameInput(userName)
        while(!validUserName){
            print("Wrong input of name \nTry again: ")
            var userName = readLine()
            if(userInput.userNameInput(userName)){
                validUserName = true
            }
        }

        print("Choose deck(name): ")
        var deckName = readLine()
        userInput.deckNameInput(deckName)

    }
}
