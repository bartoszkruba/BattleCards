import models.Deck
import models.Field
import models.Hand
import models.Player

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utilities.Utils
import kotlin.collections.ArrayList
import kotlin.random.Random

internal class GameTest {
    private lateinit var player1: Player
    private lateinit var player2: Player

    @Test
    internal fun nextTurn() {
        createMockData()
        var game: Game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.whitePlayer.mana = 0
        game.blackPlayer.mana = 0
        game.nextTurn()
        assertEquals(2, game.turn, "Turn should have increased by one")
        assertEquals(Settings.PLAYER_MANA, game.whitePlayer.mana)
        assertEquals(Settings.PLAYER_MANA, game.blackPlayer.mana)
    }

    @Test
    internal fun placeCardOnFieldTest(){
        createMockData()
        var game: Game = Game(player1.deck, player2.deck, player1.name, player2.name)

        for(i in 1..Settings.FIELD_SIZE +1){
            game.whitePlayer.hand.cards = arrayListOf(player1.deck.cards[i])
            game.blackPlayer.hand.cards = arrayListOf(player2.deck.cards[i])

            var result = game.placeCardOnField(player1.hand.cards[0])
            var placedCard = player1.hand.cards[0]
            if(i > Settings.FIELD_SIZE){
                assertFalse(result,"Should be false because field is full")
            } else{
                assertTrue(result,"Should be true because field is not full")
                assertEquals(i,game.whitePlayer.field.cardsInList().size,"Should be $i because card is added")
                assertEquals(placedCard, game.whitePlayer.field.cardsInList()[i],"Card should be the same")
            }

            game.nextTurn()

            result = game.placeCardOnField(player2.hand.cards[0])
            placedCard = player2.hand.cards[0]
            if(i > Settings.FIELD_SIZE){
                assertFalse(result,"Should be false because field is full")
            } else{
                assertTrue(result,"Should be true because field is not full")
                assertEquals(i,game.blackPlayer.field.cardsInList().size,"Should be $i because card is added")
                assertEquals(placedCard, game.blackPlayer.field.cardsInList()[i],"Card should be the same")
            }
        }

        game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.whitePlayer.hand = Hand()
        assertFalse(game.placeCardOnField(player1.hand.cards[0]),"Should return false because no cards in hand")
    }

    @Test
    internal fun checkGameOverTest() {
        for(i in 0..10000) {
            var game = Game(
                Deck(getListWithRandomAmountOfCards(Settings.DECK_SIZE)),
                Deck(getListWithRandomAmountOfCards(Settings.DECK_SIZE)),
                "player1",
                "player2"
            )
            game.whitePlayer.hand = Hand(getListWithRandomAmountOfCards(Settings.HAND_SIZE))
            game.whitePlayer.field = Field(getListWithRandomAmountOfCards(Settings.FIELD_SIZE))
            game.blackPlayer.hand = Hand(getListWithRandomAmountOfCards(Settings.HAND_SIZE))
            game.blackPlayer.field = Field(getListWithRandomAmountOfCards(Settings.FIELD_SIZE))
            var expectedResult: Boolean
            expectedResult = (game.whitePlayer.deck.cardsInList().size == 0 && game.whitePlayer.field.cardsInList().size == 0 && game.whitePlayer.hand.cardsInList().size == 0)
                    || (game.blackPlayer.deck.cardsInList().size == 0 && game.blackPlayer.field.cardsInList().size == 0 && game.blackPlayer.hand.cardsInList().size == 0)

            assertEquals(expectedResult,game.checkGameOver())
        }

    }

    private fun getListWithRandomAmountOfCards(maxSize: Int): ArrayList<Card> {
        var listOfCards: ArrayList<Card> = arrayListOf()
        for (i in 1..Random.nextInt(0, maxSize)) {
            listOfCards.add(Monster("Monster", 2, 5))
        }
        return listOfCards
    }


    @Test
    internal fun gameConstructorTest() {
        createMockData()
        var game: Game = Game(player1.deck, player2.deck, player1.name, player2.name)
        assertEquals(1, game.turn)
        assertEquals("", game.status)
        assertEquals(player1.name, game.whitePlayer.name)
        assertEquals(player1.deck, game.whitePlayer.deck)
        assertEquals(player2.name, game.blackPlayer.name)
        assertEquals(player2.deck, game.blackPlayer.deck)
    }

    @Test
    fun validMovesTest() {
        val game = Game(Deck(), Deck(), "player1", "player2")
        


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
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.whitePlayer.field = player1.field
        game.blackPlayer.field = player2.field

        var index = 0
        do {
            val attacker: Monster = game.whitePlayer.field.cards[index] as Monster
            val getsAttacked: Monster = game.blackPlayer.field.cards[index] as Monster
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
            index++
        } while(index < game.blackPlayer.field.cards.size)
        println(
            """
            
            --------------------------------
            Player 2 field after attack
            
            """.trimIndent()
        )
        println(game.blackPlayer.field)
        assertEquals(3, game.blackPlayer.field.cards.size, "Dead cards wasn't removed from field")

        game.nextTurn()
        index = 0
        do {
            val attacker: Monster = game.blackPlayer.field.cards[index] as Monster
            val getsAttacked: Monster = game.whitePlayer.field.cards[index] as Monster
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
            index++
        } while(index < game.blackPlayer.field.cards.size)
        println(
            """
            
            --------------------------------
            Player 1 field after attack

            """.trimIndent()
        )
        println(game.whitePlayer.field)
        println()
        assertEquals(4, game.whitePlayer.field.cards.size, "Dead cards wasn't removed from field")
    }

    @Test
    fun `printCurrentGame() test`() {
        createMockData()
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.whitePlayer = player1
        game.blackPlayer = player2

        val testPrint = """

                     ${player1.name}

${player1.field}

_____________________________________________________

${player2.field}

                     ${player2.name}

        """.trimIndent()

//        assertEquals(println(testPrint), game.printCurrentGame())
    }

    private fun createMockData() {
        // reset players for mock data
        player1 = Player("player1")
        player2 = Player("player2")


        val players: Array<Player> = arrayOf(player1, player2)

        repeat(2) {
            val testCards: ArrayList<Monster> = arrayListOf(

                Monster("Ogre", 4, 7),
                Monster("Wolf", 3, 2),
                Monster("Ranger", 3, 4),
                Monster("Slime", 2, 2),
                Monster("Murloc", 1, 4)
            )

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