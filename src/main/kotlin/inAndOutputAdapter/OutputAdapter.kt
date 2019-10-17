package inAndOutputAdapter

import Card
import Game
import Monster
import inAndOutputAdapter.ASCII.Companion.ANSI_BLUE
import inAndOutputAdapter.ASCII.Companion.ANSI_PURPLE
import inAndOutputAdapter.ASCII.Companion.ANSI_RED
import inAndOutputAdapter.ASCII.Companion.ANSI_RESET
import models.Player
import prototype.CardLoader
import prototype.DeckPrototype


class OutputAdapter {

    companion object {


        fun printWelcome() {

            for (line in ASCII.BATTLE_CARDS.lines()) {
                println(line)
                Thread.sleep(250)
            }

            println("\n\n\n")

            for (line in ASCII.CREATED_WITH.lines()) {
                println(line)
                Thread.sleep(250)
            }

            println("\n\n\n")

            Thread.sleep(250)

            for (line in ASCII.KOTLIN.lines()) {
                println(line)
                Thread.sleep(250)
            }

            println("\n\n")

            Thread.sleep(2000)
        }

        fun printEnterName(player: Int) {
            print("Player $player: Enter Your Name (Only big and small letters, no numbers or special characters):")
        }

        fun printDeckPrototype(deck: DeckPrototype) {
            print("$ANSI_PURPLE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<$ANSI_RESET")
            print("$ANSI_PURPLE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n\n")
            println("                                                       YOUR Deck: ${deck.name}\n")

            for(line in deck.toString().lines()){
                println("                                                          $line")
            }

            print("\n$ANSI_PURPLE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<$ANSI_RESET")
            print("$ANSI_PURPLE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n\n")
        }

        fun printBoard(game: Game) {
            val whiteTurn = game.turn % 2 != 0
        }

        fun illegalInputInfo() {
            print("$ANSI_RED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<$ANSI_RESET")
            print("$ANSI_RED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n\n")

            println("                                             Invalid Input! Please Try Again.\n")

            print("$ANSI_RED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<$ANSI_RESET")
            print("$ANSI_RED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n\n")
        }

        fun printDrawCardFromDeck(card: Card) {
            print("$ANSI_BLUE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<$ANSI_RESET")
            print("$ANSI_BLUE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n\n")

            println("                                                       You Draw Card:\n")

            for (line in card.toString().lines()) {
                println("                                                         $line")
            }

            println()

            print("$ANSI_BLUE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<$ANSI_RESET")
            print("$ANSI_BLUE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n\n")
        }

        fun printAttackMenu() {

        }

        fun printGameOptions(options: Map<Int, String>) {
            print("$ANSI_BLUE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<$ANSI_RESET")
            print("$ANSI_BLUE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n\n")

            println("                                                     What Do You Want To Do?\n")

            for (entry in options.entries) {
                println("                                                     - ${entry.key}: ${entry.value}")
            }

            println()
            print("$ANSI_BLUE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<$ANSI_RESET")
            print("$ANSI_BLUE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n\n")
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
    OutputAdapter.printEnterName(1)
    println()
    OutputAdapter.printEnterName(2)
    println()

    val cardLoader = CardLoader()
    val deckPrototype = cardLoader.loadDeck("test")

    OutputAdapter.printDeckPrototype(deckPrototype)

    OutputAdapter.illegalInputInfo()

    val card = Monster(name = "Wolf", attack = 5, health = 7)

    OutputAdapter.printDrawCardFromDeck(card)

    val options = HashMap<Int, String>()
    options[1] = "Pass"
    options[2] = "Draw Card"
    options[3] = "Play Card"
    options[4] = "Attack Monster"

    OutputAdapter.printGameOptions(options)
}
