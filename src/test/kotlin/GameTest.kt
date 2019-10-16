import models.Deck
import models.Field
import models.Hand
import models.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import utilities.Utils
import java.util.*

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
        var attacker: Monster = player1.field.cardsInList()[0] as Monster
        var getsAttacked: Monster = player2.field.cardsInList()[0] as Monster
        var getsAttackedCopy: Monster = Utils.clone(getsAttacked) as Monster

        game.attackMonster(attacker, getsAttacked)
        assertEquals(getsAttackedCopy.health - attacker.attack, getsAttacked.health, "Attacked card should have new health values")
        assertNotEquals(getsAttacked, getsAttackedCopy, "Attacked card should have new health values")
        assertTrue(getsAttacked.cardId == getsAttackedCopy.cardId, "Attacked card isn't the same after attack")
    }

    private fun createMockData(){
        // reset players for mock data
        player1 = Player("1")
        player2 = Player("2")

        repeat(Settings.HAND_SIZE) {
            val monster = Monster("Murloc", CardType.MONSTER, UUID.randomUUID(), 1, 4)
            hand.addCard(monster)
        }
        repeat (Settings.DECK_SIZE) {
            val monster = Monster("Wolf", CardType.MONSTER, UUID.randomUUID(), 1, 3)
            deck.addCard(monster)
        }
        repeat (Settings.FIELD_SIZE) {
            val monster = Monster("WereWolf", CardType.MONSTER, UUID.randomUUID(), 5, 9)
            field.addCard(monster)
        }

        player1 = Player("Player1",deck,hand,field)
        player2 = Player("Player2",deck,hand,field)
    }
}