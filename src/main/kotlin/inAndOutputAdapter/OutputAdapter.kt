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
            println(delimiter(ANSI_PURPLE))
            println(centreLine("YOUR Deck: ${deck.name}") + "\n")

            for (line in deck.toString().lines()) {
                println(centreLine(line))
            }

            println()
            println(delimiter(ANSI_PURPLE))
        }

        fun printBoard(game: Game) {
            val whiteTurn = game.turn % 2 != 0
        }

        fun illegalInputInfo() {
            println(delimiter(ANSI_RED))

            println(centreLine("Invalid Input! Please Try Again.\n"))

            println(delimiter(ANSI_PURPLE))
        }

        fun printDrawCardFromDeck(card: Card) {
            println(delimiter(ANSI_BLUE))

            println(centreLine("You Draw Card:\n"))

            for (line in card.toString().lines()) {
                println(centreLine(line))
            }

            println()

            println(delimiter(ANSI_BLUE))
        }

        fun printAttackMenu() {

        }

        fun printGameOptions(options: Map<Int, String>) {
            println(delimiter(ANSI_BLUE))

            println(centreLine("What Do You Want To Do?\n"))

            for (entry in options.entries) {
                println(centreLine("- ${entry.key}: ${entry.value}"))
            }

            println()
            println(delimiter(ANSI_BLUE))
        }

        fun printGameOver(winner: Player) {
            println(delimiter(ANSI_RED))

            println(centreLine("Game Over, ${winner.mana} Wins."))

            println(delimiter(ANSI_RED))

        }

        fun delimiter(color: String): String {
            val line = "$color<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" +
                    ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>$ANSI_RESET\n"
            return centreLine(line)
        }

        private fun centreLine(line: String): String {
            val indent: Int = (ASCII.BATTLE_CARDS.lines()[1].length - line.length) / 2
            val sb = StringBuilder()
            repeat(indent) { sb.append(" ") }
            return sb.toString() + line + sb.toString()
        }
    }

}


fun clear() {
    println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
}

fun main() {
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
