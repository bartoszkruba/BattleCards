import models.Deck
import models.Field
import models.Hand
import models.Player

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utilities.Utils
import kotlin.collections.ArrayList

internal class GameTest {
    lateinit var player1: Player
    lateinit var player2: Player

    @Test
    internal fun nextTurn() {
        createMockData()
        var game: Game = Game(player1.deck,player2.deck,player1.name,player2.name)
        game.nextTurn()
        assertEquals(1, game.turn, "Turn should have increased by one")
    }


   @Test
   internal fun gameConstructorTest(){
       createMockData()
       var game: Game = Game(player1.deck,player2.deck,player1.name,player2.name)
       assertEquals(0,game.turn)
       assertEquals("",game.status)
       assertEquals(player1.name,game.whitePlayer.name)
       assertEquals(player1.deck,game.whitePlayer.deck)
       assertEquals(player2.name,game.whitePlayer.name)
       assertEquals(player2.deck,game.whitePlayer.deck)
   }

    @Test
    fun currentPlayerTest() {
        val game = Game(Deck(), Deck(), "player1", "player2")
        val whitePlayer: Player = game.whitePlayer
        val blackPlayer: Player = game.blackPlayer

        assertTrue(game.turn % 2 != 0 && game.currentPlayer() == whitePlayer)
        game.nextTurn()
        assertTrue(game.turn % 2 == 0 && game.currentPlayer() == blackPlayer)
        game.nextTurn()
        assertTrue(game.turn % 2 != 0 && game.currentPlayer() == whitePlayer)
        game.nextTurn()
        assertTrue(game.turn % 2 == 0 && game.currentPlayer() == blackPlayer)
    }


    @Test
    internal fun attackMonsterTest() {
        createMockData()
        val game = Game(Deck(), Deck(), "player1", "player2")

        repeat(player1.field.cards.size) {
            val attacker: Monster = player1.field.cards[it] as Monster
            val getsAttacked: Monster = player2.field.cards[it] as Monster
            val getsAttackedCopy: Monster = Utils.clone(getsAttacked) as Monster

            game.attackMonster(attacker, getsAttacked)
            assertEquals(
                getsAttackedCopy.health - attacker.attack,
                getsAttacked.health,
                "Attacked card didn't loose right health"
            )
            assertNotEquals(
                getsAttacked.toString(),
                getsAttackedCopy.toString(),
                "Attacked card should have new health values"
            )
            assertTrue(getsAttacked.cardId == getsAttackedCopy.cardId, "Attacked card isn't the same after attack")
        }
        assertEquals(3, player2.field.cards.size, "Dead cards wasn't removed from field")

        println(
            """
            
            --------------------------------
            Player 2 field after attack
            
            """.trimIndent()
        )
        println(player2.field)

        repeat(player2.field.cards.size) {
            val attacker: Monster = player2.field.cards[it] as Monster
            val getsAttacked: Monster = player1.field.cards[it] as Monster
            val getsAttackedCopy: Monster = Utils.clone(getsAttacked) as Monster

            game.attackMonster(attacker, getsAttacked)
            assertEquals(
                getsAttackedCopy.health - attacker.attack,
                getsAttacked.health,
                "Attacked card didn't loose right health"
            )
            assertNotEquals(
                getsAttacked.toString(),
                getsAttackedCopy.toString(),
                "Attacked card should have new health values"
            )
            assertTrue(getsAttacked.cardId == getsAttackedCopy.cardId, "Attacked card isn't the same after attack")
        }
        assertEquals(4, player1.field.cards.size, "Dead cards wasn't removed from field")

        println(
            """
            
            --------------------------------
            Player 1 field after attack

            """.trimIndent()
        )
        println(player1.field)
        println()
    }

    @Test
    fun `printCurrentGame() test`() {
        createMockData()
        val game = Game(Deck(), Deck(), "player1", "player2")
        game.whitePlayer = player1
        game.blackPlayer = player2

        val testPrint = """

                     ${player1.name}

${player1.field}

_____________________________________________________

${player2.field}

                     ${player2.name}

        """.trimIndent()

        assertEquals(println(testPrint), game.printCurrentGame())
    }

    private fun createMockData() {
        // reset players for mock data
        player1 = Player("1")
        player2 = Player("2")

        val testCards: ArrayList<Monster> = arrayListOf(
            Monster("Ogre", 4, 7),
            Monster("Wolf", 3, 2),
            Monster("Ranger", 3, 4),
            Monster("Slime", 2, 2),
            Monster("Murloc", 1, 4)
        )

        val players: Array<Player> = arrayOf(player1, player2)

        repeat(2) {
            val deck = Deck()
            val hand = Hand()
            val field = Field()

            var index = 0
            repeat(Settings.HAND_SIZE) {
                val card: Monster = Utils.clone(testCards[index++]) as Monster
                hand.addCard(card)
            }
            index = 0
            repeat(Settings.DECK_SIZE) {
                val card: Monster = Utils.clone(testCards[index++]) as Monster
                deck.addCard(card)
                if (index > 4) index = 0
            }
            index = 0
            repeat(Settings.FIELD_SIZE) {
                val card: Monster = Utils.clone(testCards[index++]) as Monster
                field.addCard(card)
            }
            players[it] = Player("Player$it", deck, hand, field)
        }
        player1 = players[0]
        player2 = players[1]
    }
}