package inAndOutputAdapter

import Card
import Game
import Monster
import factory.DeckFactory
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
                println(centreLine(line))
                Thread.sleep(250)
            }

            println("\n\n\n")

            Thread.sleep(250)

            for (line in ASCII.KOTLIN.lines()) {
                println(centreLine(line))
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

            println(delimiter(Settings.ANSI_GREEN))
            val stringsToPrint = ArrayList<String>()

            val currentPlayer: Player
            val opponent: Player

            if (whiteTurn) {
                currentPlayer = game.whitePlayer
                opponent = game.blackPlayer
            } else {
                currentPlayer = game.blackPlayer
                opponent = game.whitePlayer
            }

            stringsToPrint.add(
                "Hand: ${opponent.hand.cards.size}/${Settings.HAND_SIZE} | " +
                        "Deck: ${opponent.deck.cardsInList().size}/${opponent.deck.maxSize}"
            )
            stringsToPrint.add("*** ${opponent.name} ***")
            stringsToPrint.add("")
            stringsToPrint.add("==============================================================")
            stringsToPrint.add(opponent.field.toString())
            stringsToPrint.add("--------------------------------------------------------------")
            stringsToPrint.add(currentPlayer.field.toString())
            stringsToPrint.add("==============================================================")
            stringsToPrint.add("")
            stringsToPrint.add("Your Hand: ")
            stringsToPrint.add(currentPlayer.hand.toString())
            stringsToPrint.add("")
            stringsToPrint.add("*** ${currentPlayer.name} ***")
            stringsToPrint.add(
                "Mana: ${currentPlayer.mana}/${Settings.PLAYER_MANA} | Deck: " +
                        "${currentPlayer.deck.cardsInList().size}/${currentPlayer.deck.maxSize}"
            )
            stringsToPrint.add("")

            stringsToPrint.forEach {
                for (line in it.lines()) {
                    println(centreLine(line))
                }
            }
            println(delimiter(Settings.ANSI_GREEN))
        }

        fun illegalInputInfo() {
            println(delimiter(ANSI_RED))

            println(centreLine("Invalid Input! Please Try Again.\n"))

            println(delimiter(ANSI_RED))
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

        fun printChooseCardToAttackWith(game: Game) {
            println(delimiter(ANSI_PURPLE))

            val range = when (game.turn % 2 != 0) {
                true -> game.whitePlayer.field.cards.size
                false -> game.blackPlayer.field.cards.size
            }

            println(centreLine("Choose Monster To Attack With (1 - $range)"))
            print(centreLine("Your Choice: ").trimEnd() + " ")
        }

        fun printChooseTarget(game: Game) {
            println(delimiter(ANSI_PURPLE))

            val range = when (game.turn % 2 != 0) {
                true -> game.whitePlayer.field.cards.size
                false -> game.blackPlayer.field.cards.size
            }

            println(centreLine("Choose Target (1 - $range)"))
            print(centreLine("Your Choice: ").trimEnd() + " ")
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

            println(centreLine("Game Over, ${winner.name} Wins.\n"))

            println(delimiter(ANSI_RED))

        }

        fun delimiter(color: String): String {
            val line = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" +
                    ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"
            return "$color${centreLine(line)}$ANSI_RESET"
        }

        private fun centreLine(line: String): String {

            var newLine = line

            newLine = newLine.replace(Settings.ANSI_BLACK, "")
                .replace(Settings.ANSI_BLUE, "")
                .replace(Settings.ANSI_CYAN, "")
                .replace(Settings.ANSI_GREEN, "")
                .replace(Settings.ANSI_PURPLE, "")
                .replace(Settings.ANSI_RED, "")
                .replace(Settings.ANSI_RESET, "")
                .replace(Settings.ANSI_WHITE, "")
                .replace(Settings.ANSI_YELLOW, "")

            val indent: Int = (ASCII.BATTLE_CARDS.lines()[1].length - newLine.length) / 2
            val sb = StringBuilder()
            repeat(indent) { sb.append(" ") }
            return sb.toString() + line + sb.toString()
        }

        fun printChooseCardToPlay(game: Game) {
            println(delimiter(ANSI_PURPLE))

            val range = when (game.turn % 2 != 0) {
                true -> game.whitePlayer.hand.cardsInList().size
                false -> game.blackPlayer.hand.cardsInList().size
            }

            println(centreLine("Choose Card To Play (1 - $range)"))
            print(centreLine("Your Choice: "))

        }
    }


}


fun clear() {
    println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
}

fun main() {
    OutputAdapter.printWelcome()
    OutputAdapter.printEnterName(1)
    println()
    OutputAdapter.printEnterName(2)
    println()

    clear()

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


    val playerOne = Player("Ricardo")
    OutputAdapter.printGameOver(playerOne)

    val game = Game(
        player1Deck = DeckFactory.createDeck(deckPrototype),
        player2Deck = DeckFactory.createDeck(deckPrototype),
        player1Name = "Ricardo",
        player2Name = "Dennis"
    )

    OutputAdapter.printGameOptions(game.validMoves())

    OutputAdapter.printChooseCardToAttackWith(game)
    println()
    OutputAdapter.printChooseTarget(game)
    println()

    val white = game.whitePlayer
    val black = game.blackPlayer

    white.deck.shuffleDeck()
    black.deck.shuffleDeck()

    repeat(4) {
        white.hand.addCard(white.deck.drawCard()!!)
    }

    repeat(0) {
        black.hand.addCard(black.deck.drawCard()!!)
    }

    repeat(5) {
        white.field.addCard(white.deck.drawCard()!!)
    }

    repeat(0) {
        black.field.addCard(black.deck.drawCard()!!)
    }

    game.nextTurn()

    OutputAdapter.printBoard(game)

    OutputAdapter.printChooseCardToPlay(game)
}
