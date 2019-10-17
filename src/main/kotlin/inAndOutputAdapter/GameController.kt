package inAndOutputAdapter

class GameController {

    fun printMainScreen() {
        //Output.printWelcome()
        //Output.printEnterName(1)
        print("Add player 1 name: ")
        var playerNameOne = readLine()
        var validPlayerOneName = Input.readName(playerNameOne!!)
        while(validPlayerOneName == null){
            //Output.printIlegalInput()
            print("Wrong input of name \nTry again: ")
            playerNameOne = readLine()
            if(Input.readName(playerNameOne!!) != null){
                validPlayerOneName = playerNameOne
            }
        }

        print("Add player 2 name: ")
        var playerNameTwo = readLine()
        var validPlayerTwoName = Input.readName(playerNameTwo!!)
        while(validPlayerTwoName == null){
            //Output.printIlegalInput()
            print("Wrong input of name \nTry again: ")
            playerNameTwo = readLine()
            if(Input.readName(playerNameTwo!!) != null){
                validPlayerTwoName = playerNameTwo
            }
        }

    }

}