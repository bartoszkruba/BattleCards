import models.Deck
import models.Field
import models.Hand
import models.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GameTest {

    var deck:Deck = Deck()
    var hand:Hand = Hand()
    var field:Field = Field()
    var testCard1:Monster = Monster("Monster")
    var testCard2:Monster = Monster("NoMonster")
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
        var game:Game = Game()
        var attackedCard:Monster = game.attackMonster(testCard1,testCard2)
        assertEquals(testCard2.health - testCard1.attack,testCard2.health)
        assertTrue(attackedCard !== testCard2)
        assertTrue(attackedCard.cardId == testCard2.cardId)

    }

    private fun createMockData(){
        for (i in 1..Settings.HAND_SIZE){
            val monster:Monster = Monster("Monster")
            hand.addCard(monster)
        }
        for (i in 1..Settings.DECK_SIZE){
            val monster:Monster = Monster("Monster")
            deck.addCard(monster)
        }
        for (i in 1..Settings.FIELD_SIZE){
            val monster:Monster = Monster("Monster")
            field.addCard(monster)
        }

        player1 = Player("Player1",deck,hand,field)
        player2 = Player("Player2",deck,hand,field)
    }
}