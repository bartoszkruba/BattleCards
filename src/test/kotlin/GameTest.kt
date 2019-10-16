import models.Deck
import models.Field
import models.Hand
import models.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import utilities.Utils
import java.util.*
import kotlin.collections.ArrayList

internal class GameTest {

    var deck:Deck = Deck()
    var hand:Hand = Hand()
    var field:Field = Field()
    lateinit var player1:Player
    lateinit var player2: Player


    @Test
    internal fun nextTurn() {
        var game:Game = Game()
        game.nextTurn()
        assertEquals(1,game.turn,"Turn should have increased by one")
    }

    @Test
    internal fun newGameTest(){
        createMockData()
        var game:Game = Game()
        game.turn = 2
        game.status = "asdas"
        game.newGame(player1,player2)
        assertEquals(0,game.turn)
        assertEquals("",game.status)
        assertEquals(player1,game.whitePlayer)
        assertEquals(player2,game.blackPlayer)
    }

    @Test
    internal fun attackMonsterTest(){
        createMockData()
        var game = Game()

        repeat(player1.field.cards.size) {
            var attacker: Monster = player1.field.cards[it] as Monster
            var getsAttacked: Monster = player2.field.cards[it] as Monster
            var getsAttackedCopy: Monster = Utils.clone(getsAttacked) as Monster

            game.attackMonster(attacker, getsAttacked)
            assertEquals(getsAttackedCopy.health - attacker.attack, getsAttacked.health, "Attacked card didn't loose right health")
            assertNotEquals(getsAttacked.toString(), getsAttackedCopy.toString(), "Attacked card should have new health values")
            assertTrue(getsAttacked.cardId == getsAttackedCopy.cardId, "Attacked card isn't the same after attack")
            assertEquals(3, player2.field.cards.size, "Dead cards wasn't removed from field")
        }

        println("""
            
            --------------------------------
            Player 2 field
            
            """.trimIndent())
        println(player2.field)

        repeat(player2.field.cards.size) {
            var attacker: Monster = player2.field.cards[it] as Monster
            var getsAttacked: Monster = player1.field.cards[it] as Monster
            var getsAttackedCopy: Monster = Utils.clone(getsAttacked) as Monster

            game.attackMonster(attacker, getsAttacked)
            assertEquals(getsAttackedCopy.health - attacker.attack, getsAttacked.health, "Attacked card didn't loose right health")
            assertNotEquals(getsAttacked.toString(), getsAttackedCopy.toString(), "Attacked card should have new health values")
            assertTrue(getsAttacked.cardId == getsAttackedCopy.cardId, "Attacked card isn't the same after attack")
            assertEquals(3, player2.field.cards.size, "Dead cards wasn't removed from field")
        }

        println("""
            
            --------------------------------
            Player 1 field

            """.trimIndent())
        println(player1.field)
        println()
    }

    private fun createMockData(){
        // reset players for mock data
        player1 = Player("1")
        player2 = Player("2")

        val testCards: ArrayList<Monster> = arrayListOf(
            Monster("Ogre", CardType.MONSTER, UUID.randomUUID(), 4, 7),
            Monster("Wolf", CardType.MONSTER, UUID.randomUUID(), 3, 2),
            Monster("Ranger", CardType.MONSTER, UUID.randomUUID(), 3, 4),
            Monster("Slime", CardType.MONSTER, UUID.randomUUID(), 2, 2),
            Monster("Murloc", CardType.MONSTER, UUID.randomUUID(), 1, 4)
        )
        var index = 0

        repeat(Settings.HAND_SIZE) {
            hand.addCard(testCards[index++])
        }
        index = 0
        repeat (Settings.DECK_SIZE) {
            deck.addCard(testCards[index++])
            if(index > 4) index = 0
        }
        index = 0
        repeat (Settings.FIELD_SIZE) {
            field.addCard(testCards[index++])
        }

        player1 = Player("Player1",deck,hand,field)
        player2 = Player("Player2",deck,hand,field)
    }
}