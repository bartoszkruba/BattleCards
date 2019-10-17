package inAndOutputAdapter

import models.Player
import prototype.DeckPrototype

class OutputAdapter {

    companion object {
        fun printWelcome() {
            print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
            println(
                """
 ______     ______     ______   ______   __         ______        ______     ______     ______     _____     ______    
/\  == \   /\  __ \   /\__  _\ /\__  _\ /\ \       /\  ___\      /\  ___\   /\  __ \   /\  == \   /\  __-.  /\  ___\   
\ \  __<   \ \  __ \  \/_/\ \/ \/_/\ \/ \ \ \____  \ \  __\      \ \ \____  \ \  __ \  \ \  __<   \ \ \/\ \ \ \___  \  
 \ \_____\  \ \_\ \_\    \ \_\    \ \_\  \ \_____\  \ \_____\     \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \____-  \/\_____\ 
  \/_____/   \/_/\/_/     \/_/     \/_/   \/_____/   \/_____/      \/_____/   \/_/\/_/   \/_/ /_/   \/____/   \/_____/ 
                                                                                                                       
"""
            )

            print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
        }

        fun printEnterName() {

        }

        fun printDeckPrototype(deck: DeckPrototype) {

        }

//        fun printBoard(game: Game) {
//
//        }

        fun illegalInputInfo() {

        }

        fun printDrawCardFromDeck() {

        }

        fun printAttackMenu() {

        }

        fun printGameOptions(options: Map<Int, String>) {

        }

        fun printGameOver(winner: Player) {

        }
    }

}

fun clear() {
    println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
}

fun main() {
    println("Welcome Screen")
    OutputAdapter.printWelcome()
    clear()
}
